package lodsve.mybatis.datasource;

import lodsve.core.utils.StringUtils;
import lodsve.mybatis.configs.annotations.EnableMyBatis;
import lodsve.mybatis.exception.MyBatisException;
import lodsve.mybatis.key.IDGenerator;
import lodsve.mybatis.key.mysql.MySQLIDGenerator;
import lodsve.mybatis.key.oracle.OracleIDGenerator;
import lodsve.mybatis.plugins.pagination.PaginationInterceptor;
import lodsve.mybatis.type.TypeHandlerScanner;
import org.apache.commons.lang.ArrayUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建多数据源相关bean.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/12/14 下午4:42
 */
public class MyBatisConfigutaionBuilder {
    private static final String BASE_PACKAGES_ATTRIBUTE_NAME = "basePackages";
    private static final String ENUMS_LOCATIONS_ATTRIBUTE_NAME = "enumsLocations";
    private static final String PLUGINS_ATTRIBUTE_NAME = "plugins";
    private static final String DATA_SOURCE_ATTRIBUTE_NAME = "dataSource";
    private static final String DATA_SOURCE_NAME_ATTRIBUTE_NAME = "value";
    private static final String DATA_SOURCE_DEFAULT_ATTRIBUTE_NAME = "isDefault";

    private static final String DATA_SOURCE_NAME = "lodsveDataSource";

    private AnnotationAttributes attributes;
    private AnnotationMetadata metadata;

    private MyBatisConfigutaionBuilder(AnnotationMetadata metadata) {
        this.metadata = metadata;

        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(EnableMyBatis.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", EnableMyBatis.class.getName(), metadata.getClassName()));
        this.attributes = attributes;
    }

    private Map<String, BeanDefinition> generateDataSource() {
        Map<String, BeanDefinition> beanDefinitions = new HashMap<>(16);
        AnnotationAttributes[] dataSources = attributes.getAnnotationArray(DATA_SOURCE_ATTRIBUTE_NAME);
        if (null == dataSources || dataSources.length == 0) {
            throw new MyBatisException(102005, "can't find any datasource!");
        }

        Map<String, javax.sql.DataSource> dataSourceObjects = new HashMap<>(dataSources.length);
        String defaultDataSourceKey = "";
        BeanDefinition defaultDataSource = null;
        for (AnnotationAttributes dataSource : dataSources) {
            String name = dataSource.getString(DATA_SOURCE_NAME_ATTRIBUTE_NAME);
            boolean isDefault = dataSource.getBoolean(DATA_SOURCE_DEFAULT_ATTRIBUTE_NAME);

            BeanDefinition dsBeanDefinition = new RdbmsDataSourceBeanDefinitionFactory(name).build();

            if (isDefault) {
                defaultDataSourceKey = name;
                defaultDataSource = dsBeanDefinition;
            }

            beanDefinitions.put(name, dsBeanDefinition);
            dataSourceObjects.put(name, new RdbmsDataSourceFactory(name).build());
        }

        if (defaultDataSource == null) {
            throw new MyBatisException(102006, "no default dataSource!");
        }

        // 动态数据源
        BeanDefinitionBuilder dynamicDataSource = BeanDefinitionBuilder.genericBeanDefinition(DynamicDataSource.class);
        dynamicDataSource.addPropertyValue("targetDataSources", dataSourceObjects);
        dynamicDataSource.addPropertyReference("defaultTargetDataSource", defaultDataSourceKey);

        beanDefinitions.put(DATA_SOURCE_NAME, dynamicDataSource.getBeanDefinition());

        // 生成IDGenerator
        String driverClassName = (String) defaultDataSource.getPropertyValues().get("driverClassName");
        Class<? extends IDGenerator> clazz;
        if (StringUtils.contains(driverClassName, "mysql")) {
            clazz = MySQLIDGenerator.class;
        } else if (StringUtils.contains(driverClassName, "oracle")) {
            clazz = OracleIDGenerator.class;
        } else {
            clazz = MySQLIDGenerator.class;
        }

        BeanDefinitionBuilder idGenerator = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        idGenerator.addPropertyReference("dataSource", DATA_SOURCE_NAME);
        beanDefinitions.put("idGenerator", idGenerator.getBeanDefinition());

        return beanDefinitions;
    }

    private Map<String, BeanDefinition> findMyBatisBeanDefinitions() {
        String[] enumsLocations = attributes.getStringArray(ENUMS_LOCATIONS_ATTRIBUTE_NAME);
        String[] basePackages = attributes.getStringArray(BASE_PACKAGES_ATTRIBUTE_NAME);
        AnnotationAttributes[] plugins = attributes.getAnnotationArray(PLUGINS_ATTRIBUTE_NAME);

        if (ArrayUtils.isEmpty(enumsLocations)) {
            enumsLocations = findDefaultPackage(metadata);
        }
        if (ArrayUtils.isEmpty(basePackages)) {
            basePackages = findDefaultPackage(metadata);
        }

        Map<String, BeanDefinition> beanDefinitions = new HashMap<>(16);

        BeanDefinitionBuilder sqlSessionFactoryBean = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionFactoryBean.class);

        sqlSessionFactoryBean.addPropertyReference("dataSource", DATA_SOURCE_NAME);
        sqlSessionFactoryBean.addPropertyValue("mapperLocations", "classpath*:/META-INF/mybatis/**/*Mapper.xml");
        sqlSessionFactoryBean.addPropertyValue("configLocation", "classpath:/META-INF/mybatis/mybatis.xml");
        TypeHandlerScanner scanner = new TypeHandlerScanner();
        sqlSessionFactoryBean.addPropertyValue("typeHandlers", scanner.find(StringUtils.join(enumsLocations, ",")));
        List<Interceptor> pluginsList = new ArrayList<>(plugins.length);
        List<Class<? extends Interceptor>> clazz = new ArrayList<>(plugins.length);
        for (AnnotationAttributes plugin : plugins) {
            Class<? extends Interceptor> pluginClass = plugin.getClass("value");
            AnnotationAttributes[] params = plugin.getAnnotationArray("params");

            clazz.add(pluginClass);
            Interceptor interceptor = BeanUtils.instantiate(pluginClass);
            BeanWrapper beanWrapper = new BeanWrapperImpl(interceptor);
            for (AnnotationAttributes param : params) {
                String key = param.getString("key");
                String value = param.getString("value");

                PropertyDescriptor descriptor = beanWrapper.getPropertyDescriptor(key);
                Method writeMethod = descriptor.getWriteMethod();
                Method readMethod = descriptor.getReadMethod();
                writeMethod.setAccessible(true);
                try {
                    Class<?> returnType = readMethod.getReturnType();
                    Object valueObject = value;
                    if (Integer.class.equals(returnType) || int.class.equals(returnType)) {
                        valueObject = Integer.valueOf(value);
                    } else if (Long.class.equals(returnType) || long.class.equals(returnType)) {
                        valueObject = Long.valueOf(value);
                    } else if (Boolean.class.equals(returnType) || boolean.class.equals(returnType)) {
                        valueObject = Boolean.valueOf(value);
                    } else if (Double.class.equals(returnType) || double.class.equals(returnType)) {
                        valueObject = Double.valueOf(value);
                    }

                    writeMethod.invoke(interceptor, valueObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            pluginsList.add(interceptor);
        }
        if (!clazz.contains(PaginationInterceptor.class)) {
            pluginsList.add(BeanUtils.instantiate(PaginationInterceptor.class));
        }

        sqlSessionFactoryBean.addPropertyValue("plugins", pluginsList);

        BeanDefinitionBuilder scannerConfigurerBean = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
        scannerConfigurerBean.addPropertyValue("basePackage", StringUtils.join(basePackages, ","));
        scannerConfigurerBean.addPropertyValue("annotationClass", Repository.class);
        scannerConfigurerBean.addPropertyValue("sqlSessionFactoryBeanName", "sqlSessionFactory");

        beanDefinitions.put("sqlSessionFactory", sqlSessionFactoryBean.getBeanDefinition());
        beanDefinitions.put("mapperScannerConfigurer", scannerConfigurerBean.getBeanDefinition());

        return beanDefinitions;
    }

    private String[] findDefaultPackage(AnnotationMetadata annotationMetadata) {
        String className = annotationMetadata.getClassName();
        try {
            Class<?> clazz = ClassUtils.forName(className, getClass().getClassLoader());
            return new String[]{ClassUtils.getPackageName(clazz)};
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    public static class Builder {
        private AnnotationMetadata metadata;

        public Builder setMetadata(AnnotationMetadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public Map<String, BeanDefinition> build() {
            Map<String, BeanDefinition> beanDefinitions = new HashMap<>(16);
            MyBatisConfigutaionBuilder builder = new MyBatisConfigutaionBuilder(metadata);

            beanDefinitions.putAll(builder.generateDataSource());
            beanDefinitions.putAll(builder.findMyBatisBeanDefinitions());

            return beanDefinitions;
        }
    }
}

package lodsve.mybatis.configs;

import com.p6spy.engine.spy.P6DataSource;
import lodsve.core.config.ProfileConfig;
import lodsve.core.utils.StringUtils;
import lodsve.mybatis.configs.annotations.EnableMyBatis;
import lodsve.mybatis.type.TypeHandlerScanner;
import org.apache.commons.lang.ArrayUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.flywaydb.core.Flyway;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.*;

/**
 * 动态创建mybatis的配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/19 下午8:01
 */
public class MyBatisBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    private static final String DATA_SOURCE_ATTRIBUTE_NAME = "dataSource";
    private static final String BASE_PACKAGES_ATTRIBUTE_NAME = "basePackages";
    private static final String ENUMS_LOCATIONS_ATTRIBUTE_NAME = "enumsLocations";
    private static final String USE_FLYWAY_ATTRIBUTE_NAME = "useFlyway";
    private static final String MIGRATION_ATTRIBUTE_NAME = "migration";
    private static final String PLUGINS_ATTRIBUTE_NAME = "plugins";
    private static String dataSource;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableMyBatis.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", EnableMyBatis.class.getName(), importingClassMetadata.getClassName()));
        Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

        dataSource = attributes.getString(DATA_SOURCE_ATTRIBUTE_NAME);
        beanDefinitions.putAll(generateDataSource(dataSource));

        String[] enumsLocations = attributes.getStringArray(ENUMS_LOCATIONS_ATTRIBUTE_NAME);
        String[] basePackages = attributes.getStringArray(BASE_PACKAGES_ATTRIBUTE_NAME);

        if (ArrayUtils.isEmpty(enumsLocations)) {
            enumsLocations = findDefaultPackage(importingClassMetadata);
        }
        if (ArrayUtils.isEmpty(basePackages)) {
            basePackages = findDefaultPackage(importingClassMetadata);
        }

        boolean useFlyway = attributes.getBoolean(USE_FLYWAY_ATTRIBUTE_NAME);
        String migration = attributes.getString(MIGRATION_ATTRIBUTE_NAME);

        if (useFlyway) {
            beanDefinitions.putAll(findFlyWayBeanDefinitions(migration));
        }

        beanDefinitions.putAll(findMyBatisBeanDefinitions(useFlyway, basePackages, enumsLocations, attributes.getClassArray(PLUGINS_ATTRIBUTE_NAME)));

        registerBeanDefinitions(beanDefinitions, registry);
    }

    private Map<String, BeanDefinition> generateDataSource(String dataSource) {
        boolean p6spy = ProfileConfig.getProfile("p6spy");
        if (p6spy) {
            Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

            beanDefinitions.put("realDataSource", new RdbmsDataSourceBeanDefinitionFactory(dataSource).build());

            BeanDefinitionBuilder p6spyDataSource = BeanDefinitionBuilder.genericBeanDefinition(P6DataSource.class);
            p6spyDataSource.addConstructorArgReference("realDataSource");

            beanDefinitions.put(dataSource, p6spyDataSource.getBeanDefinition());

            return beanDefinitions;
        }

        return Collections.singletonMap(dataSource, new RdbmsDataSourceBeanDefinitionFactory(dataSource).build());
    }

    private Map<String, BeanDefinition> findFlyWayBeanDefinitions(String migration) {
        Map<String, BeanDefinition> beanDefinitions = new HashMap<>();
        BeanDefinitionBuilder flywayBean = BeanDefinitionBuilder.genericBeanDefinition(Flyway.class);
        flywayBean.setInitMethodName("migrate");
        flywayBean.addPropertyReference("dataSource", dataSource);
        flywayBean.addPropertyValue("locations", migration);

        beanDefinitions.put(dataSource + "Flyway", flywayBean.getBeanDefinition());

        return beanDefinitions;
    }

    private Map<String, BeanDefinition> findMyBatisBeanDefinitions(boolean useFlyway, String[] basePackages, String[] enumsLocations, Class<?>[] pluginClasses) {
        Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

        BeanDefinitionBuilder sqlSessionFactoryBean = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionFactoryBean.class);
        if (useFlyway) {
            sqlSessionFactoryBean.addDependsOn(dataSource + "Flyway");
        }
        sqlSessionFactoryBean.addPropertyReference("dataSource", dataSource);
        sqlSessionFactoryBean.addPropertyValue("mapperLocations", "classpath*:/META-INF/mybatis/**/*Mapper.xml");
        sqlSessionFactoryBean.addPropertyValue("configLocation", "classpath:/META-INF/mybatis/mybatis.xml");
        TypeHandlerScanner scanner = new TypeHandlerScanner();
        sqlSessionFactoryBean.addPropertyValue("typeHandlers", scanner.find(StringUtils.join(enumsLocations, ",")));
        List<Interceptor> plugins = new ArrayList<>(pluginClasses.length);
        for (Class<?> clazz : pluginClasses) {
            if (!(Interceptor.class.isAssignableFrom(clazz))) {
                continue;
            }

            plugins.add((Interceptor) BeanUtils.instantiate(clazz));
        }
        sqlSessionFactoryBean.addPropertyValue("plugins", plugins);

        BeanDefinitionBuilder scannerConfigurerBean = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
        scannerConfigurerBean.addPropertyValue("basePackage", StringUtils.join(basePackages, ","));
        scannerConfigurerBean.addPropertyValue("annotationClass", Repository.class);
        scannerConfigurerBean.addPropertyValue("sqlSessionFactoryBeanName", dataSource + "SqlSessionFactory");

        beanDefinitions.put(dataSource + "SqlSessionFactory", sqlSessionFactoryBean.getBeanDefinition());
        beanDefinitions.put(dataSource + "MapperScannerConfigurer", scannerConfigurerBean.getBeanDefinition());

        return beanDefinitions;
    }

    private void registerBeanDefinitions(Map<String, BeanDefinition> beanDefinitions, BeanDefinitionRegistry registry) {
        for (Iterator<Map.Entry<String, BeanDefinition>> it = beanDefinitions.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, BeanDefinition> entry = it.next();
            String beanName = entry.getKey();
            BeanDefinition bean = entry.getValue();

            if (registry.containsBeanDefinition(beanName)) {
                continue;
            }

            registry.registerBeanDefinition(beanName, bean);
        }
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
}

/*
 * Copyright (C) 2018  Sun.Hao
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.mybatis.configs;

import com.p6spy.engine.spy.P6DataSource;
import lodsve.core.properties.Profiles;
import lodsve.core.utils.StringUtils;
import lodsve.mybatis.configs.annotations.EnableMyBatis;
import lodsve.mybatis.datasource.builder.RdbmsDataSourceBeanDefinitionBuilder;
import lodsve.mybatis.datasource.dynamic.DynamicDataSource;
import lodsve.mybatis.exception.MyBatisException;
import lodsve.mybatis.key.IDGenerator;
import lodsve.mybatis.key.mysql.MySQLIDGenerator;
import lodsve.mybatis.key.oracle.OracleIDGenerator;
import lodsve.mybatis.p6spy.LodsveP6OptionsSource;
import lodsve.mybatis.type.TypeHandlerScanner;
import lodsve.mybatis.utils.Constants;
import org.apache.commons.lang.ArrayUtils;
import org.flywaydb.core.Flyway;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建多数据源相关bean.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2017/12/14 下午4:42
 */
public class MyBatisConfigurationBuilder {
    private AnnotationAttributes attributes;
    private AnnotationMetadata metadata;
    private boolean useFlyway;
    private String migration;

    private MyBatisConfigurationBuilder(AnnotationMetadata metadata) {
        this.metadata = metadata;

        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(EnableMyBatis.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", EnableMyBatis.class.getName(), metadata.getClassName()));
        this.attributes = attributes;
        this.useFlyway = attributes.getBoolean(Constants.USE_FLYWAY_ATTRIBUTE_NAME);
        this.migration = attributes.getString(Constants.MIGRATION_ATTRIBUTE_NAME);
    }

    private Map<String, BeanDefinition> generateDataSource() {
        Map<String, BeanDefinition> beanDefinitions = new HashMap<>(16);
        String[] dataSources = attributes.getStringArray(Constants.DATA_SOURCE_ATTRIBUTE_NAME);
        if (null == dataSources || dataSources.length == 0) {
            throw new MyBatisException(102005, "can't find any datasource!");
        }

        // 组装一些信息
        DataSourceBean dataSourceBean = new DataSourceBean(dataSources);
        String defaultDataSourceKey = dataSourceBean.getDefaultDataSourceKey();
        BeanDefinition defaultDataSource = dataSourceBean.getDefaultDataSource();
        beanDefinitions.putAll(dataSourceBean.getBeanDefinitions());

        // 动态数据源
        BeanDefinitionBuilder dynamicDataSource = BeanDefinitionBuilder.genericBeanDefinition(DynamicDataSource.class);
        dynamicDataSource.addConstructorArgValue(dataSourceBean.getDataSourceNames());
        dynamicDataSource.addConstructorArgValue(defaultDataSourceKey);

        boolean p6spy = Profiles.getProfile("p6spy");
        if (p6spy) {
            // 使用p6spy
            beanDefinitions.put(Constants.REAL_DATA_SOURCE_BEAN_NAME, dynamicDataSource.getBeanDefinition());

            BeanDefinitionBuilder p6spyDataSource = BeanDefinitionBuilder.genericBeanDefinition(P6DataSource.class);
            p6spyDataSource.addConstructorArgReference(Constants.REAL_DATA_SOURCE_BEAN_NAME);

            beanDefinitions.put(Constants.DATA_SOURCE_BEAN_NAME, p6spyDataSource.getBeanDefinition());

            // 初始化配置
            LodsveP6OptionsSource.init();
        } else {
            beanDefinitions.put(Constants.DATA_SOURCE_BEAN_NAME, dynamicDataSource.getBeanDefinition());
        }


        // 生成IDGenerator
        String driverClassName = (String) defaultDataSource.getPropertyValues().get("driverClassName");
        Class<? extends IDGenerator> clazz;
        if (StringUtils.contains(driverClassName, Constants.MYSQL)) {
            clazz = MySQLIDGenerator.class;
        } else if (StringUtils.contains(driverClassName, Constants.ORACLE)) {
            clazz = OracleIDGenerator.class;
        } else {
            clazz = MySQLIDGenerator.class;
        }

        BeanDefinitionBuilder idGenerator = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        idGenerator.addPropertyReference("dataSource", Constants.DATA_SOURCE_BEAN_NAME);
        beanDefinitions.put(Constants.ID_GENERATOR_BANE_NAME, idGenerator.getBeanDefinition());

        return beanDefinitions;
    }

    private Map<String, BeanDefinition> findFlyWayBeanDefinitions() {
        Map<String, BeanDefinition> beanDefinitions = new HashMap<>(16);
        if (!useFlyway) {
            return beanDefinitions;
        }

        BeanDefinitionBuilder flywayBean = BeanDefinitionBuilder.genericBeanDefinition(Flyway.class);
        flywayBean.setInitMethodName("migrate");
        flywayBean.addPropertyReference("dataSource", Constants.DATA_SOURCE_BEAN_NAME);
        flywayBean.addPropertyValue("locations", migration);

        beanDefinitions.put(Constants.FLYWAY_BEAN_NAME, flywayBean.getBeanDefinition());

        return beanDefinitions;
    }

    private Map<String, BeanDefinition> findMyBatisBeanDefinitions() {
        String[] enumsLocations = attributes.getStringArray(Constants.ENUMS_LOCATIONS_ATTRIBUTE_NAME);
        String[] basePackages = attributes.getStringArray(Constants.BASE_PACKAGES_ATTRIBUTE_NAME);
        String[] mapperLocations = attributes.getStringArray(Constants.MAPPER_LOCATIONS_ATTRIBUTE_NAME);
        String configLocation = attributes.getString(Constants.CONFIG_LOCATION_ATTRIBUTE_NAME);

        if (ArrayUtils.isEmpty(enumsLocations)) {
            enumsLocations = findDefaultPackage(metadata);
        }
        if (ArrayUtils.isEmpty(basePackages)) {
            basePackages = findDefaultPackage(metadata);
        }

        Map<String, BeanDefinition> beanDefinitions = new HashMap<>(16);

        BeanDefinitionBuilder sqlSessionFactoryBean = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionFactoryBean.class);

        if (useFlyway) {
            sqlSessionFactoryBean.addDependsOn(Constants.FLYWAY_BEAN_NAME);
        }

        sqlSessionFactoryBean.addPropertyReference("dataSource", Constants.DATA_SOURCE_BEAN_NAME);
        sqlSessionFactoryBean.addPropertyValue("mapperLocations", mapperLocations);
        sqlSessionFactoryBean.addPropertyValue("configLocation", configLocation);
        TypeHandlerScanner scanner = new TypeHandlerScanner();
        sqlSessionFactoryBean.addPropertyValue("typeHandlers", scanner.find(StringUtils.join(enumsLocations, ",")));

        BeanDefinitionBuilder scannerConfigurerBean = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
        scannerConfigurerBean.addPropertyValue("basePackage", StringUtils.join(basePackages, ","));
        scannerConfigurerBean.addPropertyValue("annotationClass", Repository.class);
        scannerConfigurerBean.addPropertyValue("sqlSessionFactoryBeanName", "sqlSessionFactory");

        beanDefinitions.put(Constants.MYBATIS_SQL_SESSION_FACTORY_BANE_NAME, sqlSessionFactoryBean.getBeanDefinition());
        beanDefinitions.put(Constants.MAPPER_SCANNER_CONFIGURER_BANE_NAME, scannerConfigurerBean.getBeanDefinition());

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

        Builder setMetadata(AnnotationMetadata metadata) {
            this.metadata = metadata;
            return this;
        }

        Map<String, BeanDefinition> build() {
            Map<String, BeanDefinition> beanDefinitions = new HashMap<>(16);
            MyBatisConfigurationBuilder builder = new MyBatisConfigurationBuilder(metadata);

            beanDefinitions.putAll(builder.generateDataSource());
            beanDefinitions.putAll(builder.findFlyWayBeanDefinitions());
            beanDefinitions.putAll(builder.findMyBatisBeanDefinitions());

            return beanDefinitions;
        }
    }

    private static class DataSourceBean {
        private String defaultDataSourceKey;
        private BeanDefinition defaultDataSource;
        private Map<String, BeanDefinition> beanDefinitions;
        private List<String> dataSourceNames;

        DataSourceBean(String[] dataSources) {
            beanDefinitions = new HashMap<>(dataSources.length);
            dataSourceNames = new ArrayList<>(dataSources.length);

            for (int i = 0; i < dataSources.length; i++) {
                String name = dataSources[i];
                BeanDefinition dsBeanDefinition = new RdbmsDataSourceBeanDefinitionBuilder(name).build();

                if (i == 0) {
                    defaultDataSourceKey = name;
                    defaultDataSource = dsBeanDefinition;
                }

                beanDefinitions.put(name, dsBeanDefinition);
                dataSourceNames.add(name);
            }
        }

        String getDefaultDataSourceKey() {
            return defaultDataSourceKey;
        }

        BeanDefinition getDefaultDataSource() {
            return defaultDataSource;
        }

        Map<String, BeanDefinition> getBeanDefinitions() {
            return beanDefinitions;
        }

        List<String> getDataSourceNames() {
            return dataSourceNames;
        }
    }
}

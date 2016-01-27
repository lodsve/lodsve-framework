package message.mybatis.configs;

import java.util.Arrays;
import message.base.utils.StringUtils;
import message.mybatis.configs.annotations.EnableMyBatis;
import message.mybatis.helper.MySQLSqlHelper;
import message.mybatis.helper.OracleSqlHelper;
import message.mybatis.key.generic.MySQLMaxValueIncrementer;
import message.mybatis.key.sequence.OracleSequenceMaxValueIncrementer;
import message.mybatis.type.TypeHandlerScanner;
import org.apache.commons.lang.ArrayUtils;
import org.flywaydb.core.Flyway;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.OracleLobHandler;
import org.springframework.jdbc.support.nativejdbc.CommonsDbcpNativeJdbcExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 动态创建mybatis的配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/19 下午8:01
 */
public class MyBatisBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    private static final String DATA_SOURCE_ATTRIBUTE_NAME = "dataSource";
    private static final String BASE_PACKAGES_ATTRIBUTE_NAME = "basePackages";
    private static final String TYPE_HANDLERS_LOCATIONS_ATTRIBUTE_NAME = "typeHandlersLocations";
    private static final String USE_FLYWAY_ATTRIBUTE_NAME = "useFlyway";
    private static final String MIGRATION_ATTRIBUTE_NAME = "migration";
    private static String dataSource;

    private static final boolean IS_MYSQL = ClassUtils.isPresent("com.mysql.jdbc.Driver", MyBatisBeanDefinitionRegistrar.class.getClassLoader());
    private static final boolean IS_ORACLE = ClassUtils.isPresent("oracle.jdbc.driver.OracleDriver", MyBatisBeanDefinitionRegistrar.class.getClassLoader());
    private static final boolean IS_SQL_SERVER = ClassUtils.isPresent("com.microsoft.sqlserver.jdbc.SQLServerDriver", MyBatisBeanDefinitionRegistrar.class.getClassLoader());

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableMyBatis.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", EnableMyBatis.class.getName(), importingClassMetadata.getClassName()));
        Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

        dataSource = attributes.getString(DATA_SOURCE_ATTRIBUTE_NAME);
        beanDefinitions.putAll(generateDataSource(dataSource));

        String[] typeHandlersLocations = attributes.getStringArray(TYPE_HANDLERS_LOCATIONS_ATTRIBUTE_NAME);
        String[] basePackages = attributes.getStringArray(BASE_PACKAGES_ATTRIBUTE_NAME);

        Class<?> introspectedClass = ((StandardAnnotationMetadata) importingClassMetadata).getIntrospectedClass();
        if (ArrayUtils.isEmpty(basePackages) && introspectedClass != null) {
            basePackages = Arrays.asList(ClassUtils.getPackageName(introspectedClass)).toArray(new String[1]);
        }
        if (ArrayUtils.isEmpty(typeHandlersLocations) && introspectedClass != null) {
            typeHandlersLocations = Arrays.asList(ClassUtils.getPackageName(introspectedClass)).toArray(new String[1]);
        }

        boolean useFlyway = attributes.getBoolean(USE_FLYWAY_ATTRIBUTE_NAME);
        String migration = attributes.getString(MIGRATION_ATTRIBUTE_NAME);

        if (IS_MYSQL) {
            beanDefinitions.putAll(findMySQLBeanDefinitions());
        } else if (IS_ORACLE) {
            beanDefinitions.putAll(findOracleBeanDefinitions());
        } else if (IS_SQL_SERVER) {
            beanDefinitions.putAll(findSQLServerBeanDefinitions());
        }

        if (useFlyway) {
            beanDefinitions.putAll(findFlyWayBeanDefinitions(migration));
        }

        beanDefinitions.putAll(findMyBatisBeanDefinitions(useFlyway, basePackages, typeHandlersLocations));

        registerBeanDefinitions(beanDefinitions, registry);
    }

    private Map<String, BeanDefinition> generateDataSource(String dataSource) {
        return Collections.singletonMap(dataSource, new RdbmsDataSourceBeanDefinitionFactory(dataSource).build());
    }

    private Map<String, BeanDefinition> findMySQLBeanDefinitions() {
        Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

        BeanDefinitionBuilder lobHandlerBean = BeanDefinitionBuilder.genericBeanDefinition(DefaultLobHandler.class);
        lobHandlerBean.setLazyInit(true);

        BeanDefinitionBuilder maxValueIncrementerBean = BeanDefinitionBuilder.genericBeanDefinition(MySQLMaxValueIncrementer.class);
        maxValueIncrementerBean.addPropertyReference("dataSource", dataSource);
        maxValueIncrementerBean.addPropertyValue("keyLength", 5);
        maxValueIncrementerBean.addPropertyValue("cacheSize", 5);

        BeanDefinitionBuilder sqlHelperBean = BeanDefinitionBuilder.genericBeanDefinition(MySQLSqlHelper.class);
        sqlHelperBean.addPropertyReference("idGenerator", dataSource + "MaxValueIncrementer");

        beanDefinitions.put(dataSource + "MysqlLobHandler", lobHandlerBean.getBeanDefinition());
        beanDefinitions.put(dataSource + "MaxValueIncrementer", maxValueIncrementerBean.getBeanDefinition());
        beanDefinitions.put(dataSource + "SqlHelper", sqlHelperBean.getBeanDefinition());

        return beanDefinitions;
    }

    private Map<String, BeanDefinition> findOracleBeanDefinitions() {
        Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

        BeanDefinitionBuilder jdbcExtractorBean = BeanDefinitionBuilder.genericBeanDefinition(CommonsDbcpNativeJdbcExtractor.class);
        jdbcExtractorBean.setLazyInit(true);

        BeanDefinitionBuilder lobHandlerBean = BeanDefinitionBuilder.genericBeanDefinition(OracleLobHandler.class);
        lobHandlerBean.addPropertyReference("nativeJdbcExtractor", dataSource + "JdbcExtractor");

        BeanDefinitionBuilder sqlHelperBean = BeanDefinitionBuilder.genericBeanDefinition(OracleSqlHelper.class);
        sqlHelperBean.addPropertyReference("lobHandler", dataSource + "OracleLobHandler");
        sqlHelperBean.addPropertyReference("idGenerator", dataSource + "MaxValueIncrementer");

        BeanDefinitionBuilder maxValueIncrementerBean = BeanDefinitionBuilder.genericBeanDefinition(OracleSequenceMaxValueIncrementer.class);
        maxValueIncrementerBean.addPropertyReference("dataSource", dataSource);
        maxValueIncrementerBean.addPropertyValue("keyLength", 5);

        beanDefinitions.put(dataSource + "JdbcExtractor", jdbcExtractorBean.getBeanDefinition());
        beanDefinitions.put(dataSource + "OracleLobHandler", lobHandlerBean.getBeanDefinition());
        beanDefinitions.put(dataSource + "SqlHelper", sqlHelperBean.getBeanDefinition());
        beanDefinitions.put(dataSource + "MaxValueIncrementer", maxValueIncrementerBean.getBeanDefinition());

        return beanDefinitions;
    }

    private Map<String, BeanDefinition> findSQLServerBeanDefinitions() {
        return Collections.emptyMap();
    }

    private Map<String, BeanDefinition> findFlyWayBeanDefinitions(String migration) {
        Map<String, BeanDefinition> beanDefinitions = new HashMap<>();
        BeanDefinitionBuilder flywayBean = BeanDefinitionBuilder.genericBeanDefinition(Flyway.class);
        flywayBean.setInitMethodName("migrate");
        flywayBean.addPropertyReference("dataSource", dataSource);
        flywayBean.addPropertyReference("locations", migration);

        beanDefinitions.put(dataSource + "Flyway", flywayBean.getBeanDefinition());

        return beanDefinitions;
    }

    private Map<String, BeanDefinition> findMyBatisBeanDefinitions(boolean useFlyway, String[] basePackages, String[] typeHandlersLocations) {
        Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

        BeanDefinitionBuilder sqlSessionFactoryBean = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionFactoryBean.class);
        if (useFlyway) {
            sqlSessionFactoryBean.addDependsOn(dataSource + "Flyway");
        }
        sqlSessionFactoryBean.addPropertyReference("dataSource", dataSource);
        sqlSessionFactoryBean.addPropertyValue("mapperLocations", "classpath*:/META-INF/mybatis/**/*Mapper.xml");
        sqlSessionFactoryBean.addPropertyValue("configLocation", "classpath:/META-INF/mybatis/mybatis.xml");
        TypeHandlerScanner scanner = new TypeHandlerScanner();
        sqlSessionFactoryBean.addPropertyValue("typeHandlers", scanner.find(StringUtils.join(typeHandlersLocations, ",")));

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

}

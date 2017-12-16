package lodsve.mybatis.configs;

/**
 * 常量.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/12/15 下午11:11
 */
public class Constant {
    private Constant() {
    }

    public static final String BASE_PACKAGES_ATTRIBUTE_NAME = "basePackages";
    public static final String ENUMS_LOCATIONS_ATTRIBUTE_NAME = "enumsLocations";
    public static final String PLUGINS_ATTRIBUTE_NAME = "plugins";
    public static final String DATA_SOURCE_ATTRIBUTE_NAME = "dataSource";
    public static final String DATA_SOURCE_NAME_ATTRIBUTE_NAME = "value";
    public static final String USE_FLYWAY_ATTRIBUTE_NAME = "useFlyway";
    public static final String MIGRATION_ATTRIBUTE_NAME = "migration";

    public static final String DATA_SOURCE_BEAN_NAME = "lodsveDataSource";
    public static final String FLYWAY_BEAN_NAME = "lodsveFlyway";
    public static final String MYSQL = "mysql";
    public static final String ORACLE = "oracle";
    public static final String SUPPORT_TRANSACTION_ATTRIBUTE_NAME = "supportTransaction";

    public static final String DRUID_DATA_SOURCE_CLASS = "com.alibaba.druid.pool.DruidDataSource";
    public static final String DBCP_DATA_SOURCE_CLASS = "org.apache.commons.dbcp.BasicDataSource";
}

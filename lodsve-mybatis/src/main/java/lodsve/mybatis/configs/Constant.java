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

    static final String BASE_PACKAGES_ATTRIBUTE_NAME = "basePackages";
    static final String ENUMS_LOCATIONS_ATTRIBUTE_NAME = "enumsLocations";
    static final String PLUGINS_ATTRIBUTE_NAME = "plugins";
    static final String DATA_SOURCE_ATTRIBUTE_NAME = "dataSource";
    static final String DATA_SOURCE_NAME_ATTRIBUTE_NAME = "value";
    static final String USE_FLYWAY_ATTRIBUTE_NAME = "useFlyway";
    static final String MIGRATION_ATTRIBUTE_NAME = "migration";

    public static final String DATA_SOURCE_BEAN_NAME = "lodsveDataSource";
    static final String REAL_DATA_SOURCE_BEAN_NAME = "lodsveRealDataSource";
    static final String FLYWAY_BEAN_NAME = "lodsveFlyway";
    static final String MYSQL = "mysql";
    static final String ORACLE = "oracle";
    static final String SUPPORT_TRANSACTION_ATTRIBUTE_NAME = "supportTransaction";

    public static final String DRUID_DATA_SOURCE_CLASS = "com.alibaba.druid.pool.DruidDataSource";
    public static final String DBCP_DATA_SOURCE_CLASS = "org.apache.commons.dbcp.BasicDataSource";
}

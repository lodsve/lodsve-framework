package message.datasource.annotations;

/**
 * 数据源类型.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/21 下午3:55
 */
public enum DataSourceType {
    /**
     * 关系型数据库
     */
    RDBMS,
    /**
     * redis
     */
    REDIS,
    /**
     * mongodb
     */
    MONGO
}

package lodsve.mybatis.key;

/**
 * make database id
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2012-4-11 上午08:10:32
 */
public interface IDGenerator {
    /**
     * get next long value
     *
     * @param sequenceName sequence name
     * @return next value
     */
    Long nextId(String sequenceName);

    enum KeyType {
        /**
         * twitter的snowflake的ID生成器实现
         */
        SNOWFLAKE,
        /**
         * MYSQL的方式
         */
        MYSQL,
        /**
         * ORACLE的方式
         */
        ORACLE
    }
}

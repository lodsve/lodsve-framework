package message.mybatis.key;

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
     * @return
     */
    long nextId();

    enum KeyType {
        /**
         * twitter的snowflake的ID生成器实现
         */
        SNOWFLAKE
    }
}

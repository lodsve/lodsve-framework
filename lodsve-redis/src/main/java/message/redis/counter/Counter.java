package message.redis.counter;

/**
 * 计数器.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/9/16 下午1:42
 */
public interface Counter {
    /**
     * 自增1位
     *
     * @param catalog 目录
     * @param key     键
     * @param times   要设置的次数
     */
    void set(String catalog, String key, Integer times);

    /**
     * 获取次数
     *
     * @param catalog 目录
     * @param key     键
     * @return 小于0, 表示计数器中没有记录, 需要查询后放入
     */
    int get(String catalog, String key);

    /**
     * 自增1
     *
     * @param catalog 目录
     * @param key     键
     * @param step    步长
     */
    void increment(String catalog, String key, int step);

    /**
     * 清除所有的次数
     */
    void flushAll();

    /**
     * 清除某一个域的次数
     *
     * @param catalog
     */
    void flush(String catalog);
}

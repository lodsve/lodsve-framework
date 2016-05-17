package lodsve.redis.timer;

/**
 * redis事件类型.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/10/6 下午2:56
 */
public interface RedisEventType<T> {
    /**
     * 获取type
     *
     * @return type
     */
    String getType();

    /**
     * 通过type解析出对象
     *
     * @param type type
     * @return 对象
     */
    T eval(final String type);
}

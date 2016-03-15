package message.redis.timer;

import java.io.Serializable;

/**
 * redis事件处理.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/9/28 下午5:15
 */
public interface RedisEventHandler {
    /**
     * 处理事件
     *
     * @param key 键
     */
    void handler(Serializable key);

    /**
     * 解析主键
     *
     * @param message
     * @return
     */
    Serializable resolveKey(String message);

    /**
     * 获取目标对象
     *
     * @return
     */
    RedisEventType getEventType();
}

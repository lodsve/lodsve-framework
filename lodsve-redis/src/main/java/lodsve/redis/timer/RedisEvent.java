package lodsve.redis.timer;

import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

/**
 * redis事件.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/9/28 下午4:28
 */
public class RedisEvent extends ApplicationEvent {
    private final Serializable key;
    private final RedisEventType type;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public RedisEvent(Object source, Serializable key, RedisEventType type) {
        super(source);
        this.key = key;
        this.type = type;
    }

    public final Serializable getKey() {
        return key;
    }

    public final RedisEventType getType() {
        return type;
    }
}

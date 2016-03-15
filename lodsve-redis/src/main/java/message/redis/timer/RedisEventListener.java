package message.redis.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 事件处理.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/9/28 下午4:27
 */
@Component
public class RedisEventListener implements ApplicationListener<RedisEvent> {
    private static final Logger logger = LoggerFactory.getLogger(RedisEventListener.class);

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(RedisEvent event) {
        logger.debug("event is '{}'", event);
        Serializable key = event.getKey();
        RedisEventType type = event.getType();
        logger.debug("the [{}] [{}] is expired!", type, key);

        RedisEventHandler handler = RedisEventUtils.getRedisEventHandler(type.getType());
        if (handler == null) {
            return;
        }

        handler.handler(key);
    }
}

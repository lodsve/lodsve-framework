package lodsve.redis.timer;

import lodsve.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * redis定时器的监听.<br/>
 * redis必须在配置文件redis.conf中设置为:<code>notify-keyspace-events Ex</code><br/>
 * also see <a href="http://blog.csdn.net/chaijunkun/article/details/27361453">Redis的Keyspace notifications功能初探</a>
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/9/28 下午4:11
 */
@Component
public class RedisTimerListener implements MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(RedisTimerListener.class);
    private static final String REDIS_KEY_SEPARATOR = "-";
    private static final String REDIS_KEY_PREFIX = "redisEvent" + REDIS_KEY_SEPARATOR;
    private static final String REDIS_SPRING_SESSION_KEY_SEPARATOR = ":expired";

    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    @Qualifier("redisTimerRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] messageChannel = message.getChannel();
        byte[] messageBody = message.getBody();
        if (messageChannel == null || messageBody == null) {
            return;
        }
        String channel = new String(messageChannel);
        if (!REDIS_SPRING_SESSION_KEY_SEPARATOR.endsWith(channel)) {
            return;
        }
        String body = new String(messageBody);
        if (!body.startsWith(REDIS_KEY_PREFIX)) {
            return;
        }

        String[] temp = StringUtils.split(body, REDIS_KEY_SEPARATOR);

        String type = temp[1], key = temp[2];

        RedisEventHandler handler = RedisEventUtils.getRedisEventHandler(type);
        Serializable id = handler.resolveKey(key);

        if (id == null) {
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Publishing Event for order " + id);
        }

        publishEvent(new RedisEvent(this, id, handler.getEventType()));
    }

    private void publishEvent(RedisEvent event) {
        try {
            this.eventPublisher.publishEvent(event);
        } catch (Throwable ex) {
            logger.error("Error publishing " + event + ".", ex);
        }
    }

    /**
     * 保存到redis
     *
     * @param key  唯一标示
     * @param ttl  失效时长(单位:秒)
     * @param type 事件类型
     */
    public void store(Serializable key, long ttl, RedisEventType type) {
        Assert.notNull(key, "key不能为空!");
        Assert.notNull(type, "type不能为空!");

        if (ttl <= 0) {
            // 抛出事件
            publishEvent(new RedisEvent(this, key, type));
            return;
        }

        BoundValueOperations<String, Object> operations = redisTemplate.boundValueOps(REDIS_KEY_PREFIX + type.getType() + REDIS_KEY_SEPARATOR + key);
        operations.set(key, ttl * 1000, TimeUnit.MILLISECONDS);
    }
}

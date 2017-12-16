package lodsve.redis.timer;

import lodsve.redis.core.connection.LodsveRedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * redis timer MessageListenerContainer.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/12/17 上午12:34
 */
public class RedisTimerMessageListenerContainer extends RedisMessageListenerContainer {
    public RedisTimerMessageListenerContainer(LodsveRedisConnectionFactory connectionFactory, RedisTimerListener listener) {
        super.setConnectionFactory(connectionFactory);
        super.addMessageListener(listener, new PatternTopic("__keyevent@*:expired"));
    }
}

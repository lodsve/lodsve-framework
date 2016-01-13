package message.redis.timer;

import message.redis.CosmosRedisConnectionFactory;
import message.redis.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/12 下午9:23
 */
@Configuration
public class RedisTimerConfiguration {
    private static final String PROJECT_NAME = "redisEvent";

    @Autowired
    private RedisTimerListener listener;
    @Autowired
    private RedisProperties properties;

    @Bean
    public RedisTemplate<String, Object> redisTimerRedisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(getRedisConnectionFactory(PROJECT_NAME));
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisEventRedisMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(getRedisConnectionFactory(PROJECT_NAME));
        container.addMessageListener(listener, new PatternTopic("__keyevent@*:expired"));

        return container;
    }

    /**
     * 获取redis连接
     *
     * @param project
     * @return
     */
    protected RedisConnectionFactory getRedisConnectionFactory(String project) {
        CosmosRedisConnectionFactory.Builder builder = new CosmosRedisConnectionFactory.Builder();
        builder.setProject(project);
        builder.setRedisProperties(properties);

        return builder.build();
    }
}

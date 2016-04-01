package message.cache.configs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Method;
import java.util.List;
import message.base.utils.StringParse;
import message.base.utils.StringUtils;
import message.cache.conditional.RedisCondition;
import message.cache.core.CacheProperties;
import message.redis.core.annotations.EnableRedis;
import message.redis.core.connection.LodsveRedisConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * 包含redis的缓存 Configuration.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016/1/19 16:05
 */
@EnableCaching
@Configuration
@EnableRedis(name = "cache")
@ComponentScan("message.cache.core")
@Conditional(RedisCondition.class)
public class WithRedisCacheConfiguration {
    @Autowired
    @Qualifier("cache")
    private LodsveRedisConnectionFactory connectionFactory;

    @Bean
    public RedisTemplate<Object, Object> redisCacheRedisTemplate() {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setKeySerializer(jackson2JsonRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }
}

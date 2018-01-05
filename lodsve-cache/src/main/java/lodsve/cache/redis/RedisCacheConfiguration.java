package lodsve.cache.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lodsve.cache.properties.CacheProperties;
import lodsve.core.properties.autoconfigure.annotations.EnableConfigurationProperties;
import lodsve.core.utils.StringUtils;
import lodsve.redis.core.annotations.EnableRedis;
import lodsve.redis.core.connection.LodsveRedisConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.util.Arrays;
import java.util.List;

/**
 * Redis.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/12/23 上午12:23
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
public class RedisCacheConfiguration {

    @Configuration
    @Import(RedisCacheBasicConfiguration.class)
    public static class RedisCacheManagerConfiguration {
        @Autowired
        private CacheProperties cacheProperties;
        @Autowired
        @Qualifier("redisCacheRedisTemplate")
        private RedisTemplate<Object, Object> redisTemplate;

        @Bean
        public CacheManager cacheManager() {
            RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
            String cacheNames = cacheProperties.getRedis().getCacheNames();
            List<String> cacheNameList = Arrays.asList(StringUtils.split(cacheNames));

            if (!cacheNameList.isEmpty()) {
                cacheManager.setCacheNames(cacheNameList);
            }

            cacheManager.afterPropertiesSet();
            return cacheManager;
        }
    }

    @Configuration
    @EnableRedis(name = "cache")
    public static class RedisCacheBasicConfiguration {
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
}

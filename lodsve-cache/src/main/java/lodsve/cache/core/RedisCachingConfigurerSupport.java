package lodsve.cache.core;

import lodsve.core.condition.ConditionalOnExpression;
import lodsve.core.utils.StringParse;
import lodsve.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * redis.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/4/1 上午9:49
 */
@Component
@ConditionalOnExpression("T(lodsve.cache.annotations.CacheModeHolder).getCacheMode() eq T(lodsve.cache.annotations.CacheMode).REDIS")
public class RedisCachingConfigurerSupport extends LodsveCachingConfigurerSupport {
    @Autowired
    @Qualifier("redisCacheRedisTemplate")
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public CacheManager cacheManager() {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        String cacheNames = cacheProperties.getCacheNames();
        List<String> cacheNameList = StringUtils.convert(cacheNames, new StringParse<String>() {
            @Override
            public String parse(String str) {
                return str;
            }
        });
        if (!cacheNameList.isEmpty()) {
            cacheManager.setCacheNames(cacheNameList);
        }

        cacheManager.afterPropertiesSet();
        return cacheManager;
    }
}

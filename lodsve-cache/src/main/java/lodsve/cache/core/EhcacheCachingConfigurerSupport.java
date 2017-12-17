package lodsve.cache.core;

import lodsve.core.condition.ConditionalOnExpression;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Component;

/**
 * ehcache.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/4/1 上午9:49
 */
@Component
@ConditionalOnExpression("T(lodsve.cache.annotations.CacheModeHolder).getCacheMode() eq T(lodsve.cache.annotations.CacheMode).EHCAHE")
public class EhcacheCachingConfigurerSupport extends LodsveCachingConfigurerSupport {
    @Override
    public CacheManager cacheManager() {
        EhCacheCacheManager cacheManager = new EhCacheCacheManager();

        net.sf.ehcache.CacheManager manager = net.sf.ehcache.CacheManager.create(cacheProperties.getEhcache().getConfiguration());
        cacheManager.setCacheManager(manager);
        cacheManager.afterPropertiesSet();

        return cacheManager;
    }
}

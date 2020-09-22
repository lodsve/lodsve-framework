package lodsve.cache.annotations;

import lodsve.cache.ehcache.EhcacheCacheConfiguration;
import lodsve.cache.memcached.MemcachedCacheConfiguration;
import lodsve.cache.oscache.OscacheCacheConfiguration;
import lodsve.cache.redis.RedisCacheConfiguration;

/**
 * 几种cache.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016/1/19 15:11
 */
public enum CacheMode {
    /**
     * redis cache
     */
    REDIS(RedisCacheConfiguration.class),
    /**
     * ehcache
     */
    EHCAHE(EhcacheCacheConfiguration.class),
    /**
     * memcached
     */
    MEMCACHED(MemcachedCacheConfiguration.class),
    /**
     * oscache
     */
    OSCACHE(OscacheCacheConfiguration.class);

    private final Class<?> cacheConfig;

    CacheMode(Class<?> cacheConfig) {
        this.cacheConfig = cacheConfig;
    }

    public Class<?> getCacheConfig() {
        return cacheConfig;
    }
}

package lodsve.cache.annotations;

import lodsve.cache.ehcache.EhcacheCacheConfiguration;
import lodsve.cache.guava.GuavaCacheConfiguration;
import lodsve.cache.memcached.MemcachedCacheConfiguration;
import lodsve.cache.oscache.OscacheCacheConfiguration;
import lodsve.cache.redis.RedisCacheConfiguration;

/**
 * 几种cache.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2016/1/19 15:11
 */
public enum CacheMode {
    /**
     * redis cache
     */
    REDIS(RedisCacheConfiguration.class),
    /**
     * guava cache
     */
    GUAVA(GuavaCacheConfiguration.class),
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

    private Class<?> cacheConfig;

    CacheMode(Class<?> cacheConfig) {
        this.cacheConfig = cacheConfig;
    }

    public Class<?> getCacheConfig() {
        return cacheConfig;
    }
}

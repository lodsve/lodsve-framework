package message.cache.memcached;

import message.cache.Cache;
import message.cache.CacheManager;
import net.spy.memcached.MemcachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Memcached Manager 实现.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 13-3-25 下午7:45
 */
public class MemcachedManagerImpl implements CacheManager {
    private static final Logger logger = LoggerFactory.getLogger(MemcachedManagerImpl.class);

    /**模拟缓存对象在某一个域中,key为域,Cache为memcached缓存对象**/
    private Map<String, Cache> caches;
    /**memcached连接服务端的客户端工具,指定服务端的地址以及端口**/
    private MemcachedClient memcachedClient;
    /**默认缓存存活时间**/
    private long defaultTimeout;
    /**从服务端取缓存对象超出这个时间就放弃从服务端取缓存对象**/
    private long cacheGetTimeout;
    /**锁对象**/
    private Object sysCache;

    public void afterPropertiesSet() throws Exception {
        caches = new HashMap<String, Cache>();
        sysCache = new Object();
    }

    public void destroy() throws Exception {
        this.caches.clear();
        this.caches = null;
        this.memcachedClient.shutdown();
        this.memcachedClient = null;
    }

    @Deprecated
    public List getCacheNames() {
        return Collections.EMPTY_LIST;
    }

    public Cache getCache(String region) {
        Cache cache = this.caches.get(region);
        if(cache == null){
            synchronized (sysCache) {
                cache = this.caches.get(region);
                if(cache == null) {
                    cache = new MemcachedImpl(this.memcachedClient, region, this.defaultTimeout, this.cacheGetTimeout);
                    this.caches.put(region, cache);
                }
            }
        }

        logger.debug("get cache for region '{}' is '{}'!", region, cache);
        return cache;
    }

    public void removeCache(String region) {
        this.caches.remove(region);
    }

    public void flush() {
        this.memcachedClient.flush();
    }

    public MemcachedClient getMemcachedClient() {
        return memcachedClient;
    }

    public void setMemcachedClient(MemcachedClient memcachedClient) {
        this.memcachedClient = memcachedClient;
    }

    public long getDefaultTimeout() {
        return defaultTimeout;
    }

    public void setDefaultTimeout(long defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    public long getCacheGetTimeout() {
        return cacheGetTimeout;
    }

    public void setCacheGetTimeout(long cacheGetTimeout) {
        this.cacheGetTimeout = cacheGetTimeout;
    }
}

package lodsve.cache.memcached;

import lodsve.cache.properties.CacheProperties.CacheConfig;
import net.spy.memcached.MemcachedClient;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Memcached CacheManager.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2018-1-10-0010 09:58
 */
public class MemcachedCacheManager extends AbstractTransactionSupportingCacheManager {
    private MemcachedClient memcachedClient;
    private List<CacheConfig> cacheConfigs;

    private ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (!CollectionUtils.isEmpty(cacheConfigs)) {
            for (CacheConfig config : cacheConfigs) {
                String name = config.getName();
                int expire = config.getExpire();

                cacheMap.putIfAbsent(config.getName(), new MemcachedCache(name, expire, memcachedClient));
            }
        }

        super.afterPropertiesSet();
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return cacheMap.values();
    }

    @Override
    public Cache getCache(String name) {
        return cacheMap.get(name);
    }

    public void setMemcachedClient(MemcachedClient memcachedClient) {
        this.memcachedClient = memcachedClient;
    }

    public void setCacheConfigs(List<CacheConfig> cacheConfigs) {
        this.cacheConfigs = cacheConfigs;
    }
}

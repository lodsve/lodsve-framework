package lodsve.cache.oscache;

import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import lodsve.cache.properties.CacheProperties.Oscahce.CacheConfig;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Oscache CacheManager.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2018-1-10-0010 16:26
 */
public class OscacheCacheManager extends AbstractTransactionSupportingCacheManager {
    private GeneralCacheAdministrator admin;
    private List<CacheConfig> cacheConfigs;

    private ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (!CollectionUtils.isEmpty(cacheConfigs)) {
            for (CacheConfig config : cacheConfigs) {
                String name = config.getName();
                int expire = config.getExpire();

                cacheMap.putIfAbsent(config.getName(), new OscacheCache(name, expire, admin));
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

    public void setAdmin(GeneralCacheAdministrator admin) {
        this.admin = admin;
    }

    public void setCacheConfigs(List<CacheConfig> cacheConfigs) {
        this.cacheConfigs = cacheConfigs;
    }
}

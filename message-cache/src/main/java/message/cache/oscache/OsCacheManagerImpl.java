package message.cache.oscache;

import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import message.base.utils.StringUtils;
import message.cache.Cache;
import message.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * oscache cache manager impl.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 13-3-30 上午4:58
 */
public class OsCacheManagerImpl implements CacheManager {
    private static final Logger logger = LoggerFactory.getLogger(OsCacheManagerImpl.class);

    /**oscache general cache administrator**/
    private static GeneralCacheAdministrator admin;

    /**ehcache configuration file path**/
    private String configuration;

    /**for synchronized**/
    private Object syncGetCacheObject;
    /**make what like region**/
    private Map<String, Cache> caches;
    /**default timeout, how long can this object stay in cache by second!**/
    private long defaultTimeout;

    /**
     * The configuration key that specifies whether we should block waiting for new
     * content to be generated, or just serve the old content instead. The default
     * behaviour is to serve the old content since that provides the best performance
     * (at the cost of serving slightly stale data).
     */
    protected final static String CACHE_BLOCKING_KEY = "cache.blocking";
    /**
     * A boolean cache configuration property that indicates whether the cache
     * should cache objects in memory. Set this property to <code>false</code>
     * to disable in-memory caching.
     */
    protected final static String CACHE_MEMORY_KEY = "cache.memory";
    /**
     * A boolean cache configuration property that indicates whether the persistent
     * cache should be unlimited in size, or should be restricted to the same size
     * as the in-memory cache. Set this property to <code>true</code> to allow the
     * persistent cache to grow without bound.
     */
    protected final static String CACHE_DISK_UNLIMITED_KEY = "cache.unlimited.disk";


    /**
     * Whether the cache blocks waiting for content to be build, or serves stale
     * content instead. This value can be specified using the {@link #CACHE_BLOCKING_KEY}
     * configuration property.
     */
    private boolean blocking = false;

    /**
     * Whether or not to store the cache entries in memory. This is configurable using the
     * {@link #CACHE_MEMORY_KEY} property.
     */
    private boolean memoryCaching = true;

    /**
     * Whether the disk cache should be unlimited in size, or matched 1-1 to the memory cache.
     * This can be set via the {@link #CACHE_DISK_UNLIMITED_KEY} configuration property.
     */
    private boolean unlimitedDiskCache = false;

    private static final String DEFAULT_OSCACHE_CONFIG = "/com/message/base/cache/oscache/oscache.properties";

    public void afterPropertiesSet() throws Exception {
        syncGetCacheObject = new Object();
        caches = new HashMap<String, Cache>();

        InputStream in = null;
        if(StringUtils.isEmpty(this.configuration) || !new File(this.configuration).exists()){
            logger.debug("file '{}' is not exist, use default config!");
            in = OsCacheManagerImpl.class.getResourceAsStream(DEFAULT_OSCACHE_CONFIG);
        } else {
            in = new FileInputStream(new File(this.configuration));
        }

        Properties p = new Properties();
        p.load(in);
        p.put(CACHE_BLOCKING_KEY, this.blocking);
        p.put(CACHE_MEMORY_KEY, this.memoryCaching);
        p.put(CACHE_DISK_UNLIMITED_KEY, this.unlimitedDiskCache);

        this.admin = new GeneralCacheAdministrator(p);
    }

    public void destroy() throws Exception {
        this.caches.clear();
        this.caches = null;
        this.admin.flushAll();
    }

    public List getCacheNames() {
        if(this.caches != null && this.caches.size() > 0){
            Set<String> keys = caches.keySet();
            List<String> cacheNames = new ArrayList<String>(keys.size());
            for(String key : keys){
                cacheNames.add(key);
            }

            return cacheNames;
        }

        return Collections.EMPTY_LIST;
    }

    public Cache getCache(String region) {
        Cache cache = this.caches.get(region);
        if(cache == null){
            synchronized (this.syncGetCacheObject){
                cache = new OsCacheImpl(admin, defaultTimeout);
                this.caches.put(region, cache);
            }
        }

        return cache;
    }

    public void removeCache(String region) {
        Cache cache = this.caches.remove(region);
        cache = null;
    }

    public void flush() {
        admin.flushAll();
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public void setDefaultTimeout(long defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    public void setBlocking(boolean blocking) {
        this.blocking = blocking;
    }

    public void setMemoryCaching(boolean memoryCaching) {
        this.memoryCaching = memoryCaching;
    }

    public void setUnlimitedDiskCache(boolean unlimitedDiskCache) {
        this.unlimitedDiskCache = unlimitedDiskCache;
    }
}

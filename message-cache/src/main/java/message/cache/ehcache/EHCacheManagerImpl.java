package message.cache.ehcache;

import message.cache.Cache;
import message.cache.CacheManager;
import message.utils.StringUtils;
import net.sf.ehcache.management.ManagementService;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;

/**
 * eh cache manager implement
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 12-4-9 上午7:30
 */
public class EHCacheManagerImpl implements CacheManager {
    private static final Logger logger = LoggerFactory.getLogger(EHCacheManagerImpl.class);

    /**
     * ehcache manager
     */
    private net.sf.ehcache.CacheManager manager;

    /**
     * ehcache configuration file path
     */
    private String configuration;

    /**
     * for synchronized
     */
    private Object syncGetCacheObject;

    private int maxElementsInMemory = 2000;
    private boolean overflowToDisk = false;
    private boolean eternal = false;
    private long timeToLiveSeconds = 60L;
    private long timeToIdleSeconds = 30L;
    private boolean diskPersistent = false;
    private long diskExpiryThreadIntervalSeconds = 8127978041470091264L;

    private boolean jmxEnable = false;
    private boolean registerCacheManager;
    private boolean registerCaches;
    private boolean registerCacheConfigurations;
    private boolean registerCacheStatistics;

    /**
     * init this class, set configuration path, set cache manager
     */
    public void afterPropertiesSet() throws Exception {
        this.syncGetCacheObject = new Object();

        if (StringUtils.isEmpty(this.configuration))
            this.manager = net.sf.ehcache.CacheManager.create();
        else
            this.manager = net.sf.ehcache.CacheManager.create(this.configuration);

        //TODO need to read and understant this code
        if (this.jmxEnable) {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

            ManagementService.registerMBeans(this.manager, mBeanServer, this.registerCacheManager, this.registerCaches,
                    this.registerCacheConfigurations, this.registerCacheStatistics);
        }
    }

    public void flush() {
        String[] names = this.manager.getCacheNames();
        logger.debug("cache names is '{}'", names.toString());

        if(names == null || names.length < 1){
            logger.warn("get null names!");
            return;
        }

        for(String name : names){
            net.sf.ehcache.Cache cache = this.manager.getCache(name);
            cache.removeAll();
        }
    }

    public Cache getCache(String region) {
        if(StringUtils.isEmpty(region)){
            logger.warn("this cache name is null!");
            return null;
        }

        net.sf.ehcache.Cache cache = this.manager.getCache(region);
        if(cache == null){
            //if cache is not exist, then create a new cache
            //lock for synchronized 
            synchronized (this.syncGetCacheObject) {
                /**
                 name - the name of the cache. Note that "default" is a reserved name for the defaultCache.
                 maxElementsInMemory - the maximum number of elements in memory, before they are evicted (0 == no limit)
                 memoryStoreEvictionPolicy - one of LRU, LFU and FIFO. Optionally null, in which case it will be set to LRU.
                 overflowToDisk - whether to use the disk store
                 diskStorePath - this parameter is ignored. CacheManager sets it using setter injection.
                 eternal - whether the elements in the cache are eternal, i.e. never expire
                 timeToLiveSeconds - the default amount of time to live for an element from its creation date
                 timeToIdleSeconds - the default amount of time to live for an element from its last accessed or modified date
                 diskPersistent - whether to persist the cache to disk between JVM restarts
                 diskExpiryThreadIntervalSeconds - how often to run the disk store expiry thread. A large number of 120 seconds plus is recommended
                 registeredEventListeners - a notification service. Optionally null, in which case a new one with no registered listeners will be created.
                 */
                cache = new net.sf.ehcache.Cache(region,     //cache scope
                        this.maxElementsInMemory,            //maxElementsInMemory
                        MemoryStoreEvictionPolicy.LRU,         //if null, defualt is LRU
                        this.overflowToDisk,                 //overflowToDisk, true or false
                        null,                                  //if use the disk store, it is the path disk you use
                        this.eternal,                         //is active at all time?
                        this.timeToLiveSeconds,              //the default amount of time to live for an element from its creation date
                        this.timeToIdleSeconds,              //the default amount of time to live for an element from its last accessed or modified date
                        this.diskPersistent,                 //whether to persist the cache to disk between JVM restarts
                        this.diskExpiryThreadIntervalSeconds, null);

                this.manager.addCache(cache);
            }
        }

        return new EHCacheImpl(cache);
    }

	public List getCacheNames() {
        String names[] = this.manager.getCacheNames();
        if(names == null || names.length < 1) {
            logger.warn("can not get any names!");
            return null;
        }

        return Arrays.asList(names);
    }

    public void removeCache(String region) {
        if(StringUtils.isEmpty(region)){
            logger.warn("given cache region is null!");
            return;
        }
        
        this.manager.removeCache(region);
    }

    /**
     * when this destory run it
     */
    public void destroy() throws Exception {
        if(this.manager != null)
            this.manager.shutdown();
    }

    public void setMaxElementsInMemory(int maxElementsInMemory) {
        this.maxElementsInMemory = maxElementsInMemory;
    }

    public void setOverflowToDisk(boolean overflowToDisk) {
        this.overflowToDisk = overflowToDisk;
    }

    public void setEternal(boolean eternal) {
        this.eternal = eternal;
    }

    public void setTimeToLiveSeconds(long timeToLiveSeconds) {
        this.timeToLiveSeconds = timeToLiveSeconds;
    }

    public void setTimeToIdleSeconds(long timeToIdleSeconds) {
        this.timeToIdleSeconds = timeToIdleSeconds;
    }

    public void setDiskPersistent(boolean diskPersistent) {
        this.diskPersistent = diskPersistent;
    }

    public void setDiskExpiryThreadIntervalSeconds(long diskExpiryThreadIntervalSeconds) {
        this.diskExpiryThreadIntervalSeconds = diskExpiryThreadIntervalSeconds;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public void setJmxEnable(boolean jmxEnable) {
        this.jmxEnable = jmxEnable;
    }

    public void setRegisterCacheManager(boolean registerCacheManager) {
        this.registerCacheManager = registerCacheManager;
    }

    public void setRegisterCaches(boolean registerCaches) {
        this.registerCaches = registerCaches;
    }

    public void setRegisterCacheConfigurations(boolean registerCacheConfigurations) {
        this.registerCacheConfigurations = registerCacheConfigurations;
    }

    public void setRegisterCacheStatistics(boolean registerCacheStatistics) {
        this.registerCacheStatistics = registerCacheStatistics;
    }
}

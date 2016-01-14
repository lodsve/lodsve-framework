package message.cache.configs;

import message.cache.CacheManager;
import message.cache.ehcache.EHCacheManagerImpl;
import message.cache.memcached.MemcachedManagerImpl;
import message.cache.oscache.OsCacheManagerImpl;
import message.cache.utils.CacheFactoryBean;
import message.config.SystemConfig;
import message.utils.PropertyPlaceholderHelper;
import message.utils.StringUtils;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.spring.MemcachedClientFactoryBean;
import net.spy.memcached.transcoders.SerializingTranscoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/14 下午9:24
 */
@Configuration
public class CacheConfiguration {
    private static final String CACHE_NAME_EHCACHE = "ehcache";
    private static final String CACHE_NAME_MEMCACHE = "memcache";
    private static final String CACHE_NAME_OSCACHE = "oscache";

    @Autowired
    private CacheProperties cacheProperties;

    @Bean
    public CacheManager cacheManager() throws Exception {
        if (!checkProperties()) {
            throw new CacheException("cache配置异常!");
        }

        if (CACHE_NAME_EHCACHE.equals(cacheProperties.getCacheName())) {
            EHCacheManagerImpl ehCacheManager = new EHCacheManagerImpl();

            ehCacheManager.setMaxElementsInMemory(2000);
            ehCacheManager.setOverflowToDisk(false);
            ehCacheManager.setEternal(false);
            ehCacheManager.setTimeToLiveSeconds(60);
            ehCacheManager.setTimeToIdleSeconds(60);
            ehCacheManager.setDiskPersistent(false);
            ehCacheManager.setDiskExpiryThreadIntervalSeconds(0);
            ehCacheManager.setConfiguration(replaceValue(cacheProperties.getEhcache().getConfiguration()));

            return ehCacheManager;
        } else if (CACHE_NAME_MEMCACHE.equals(cacheProperties.getCacheName())) {
            MemcachedManagerImpl memcachedManager = new MemcachedManagerImpl();

            memcachedManager.setDefaultTimeout(60);
            memcachedManager.setCacheGetTimeout(10);

            MemcachedClientFactoryBean clientFactoryBean = new MemcachedClientFactoryBean();
            clientFactoryBean.setServers(replaceValue(cacheProperties.getMemcache().getServers()));
            clientFactoryBean.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY);
            SerializingTranscoder transcoder = new SerializingTranscoder();
            transcoder.setCompressionThreshold(1024);
            clientFactoryBean.setTranscoder(transcoder);
            clientFactoryBean.setOpTimeout(60000);
            clientFactoryBean.setTimeoutExceptionThreshold(1998);
            clientFactoryBean.setUseNagleAlgorithm(false);

            memcachedManager.setMemcachedClient((MemcachedClient) clientFactoryBean.getObject());

            return memcachedManager;
        } else if (CACHE_NAME_OSCACHE.equals(cacheProperties.getCacheName())) {
            OsCacheManagerImpl osCacheManager = new OsCacheManagerImpl();

            osCacheManager.setConfiguration(replaceValue(cacheProperties.getOscache().getConfiguration()));
            osCacheManager.setDefaultTimeout(60);
            osCacheManager.setBlocking(false);
            osCacheManager.setMemoryCaching(true);
            osCacheManager.setUnlimitedDiskCache(false);

            return osCacheManager;
        }

        return null;
    }

    @Bean
    @Lazy
    public CacheFactoryBean abstractCache() throws Exception {
        CacheFactoryBean factoryBean = new CacheFactoryBean();
        factoryBean.setCacheManager(cacheManager());

        return factoryBean;
    }

    private boolean checkProperties() {
        if (CACHE_NAME_EHCACHE.equals(cacheProperties.getCacheName())) {
            return cacheProperties.getEhcache() != null && StringUtils.isNotEmpty(cacheProperties.getEhcache().getConfiguration());
        } else if (CACHE_NAME_MEMCACHE.equals(cacheProperties.getCacheName())) {
            return cacheProperties.getMemcache() != null && StringUtils.isNotEmpty(cacheProperties.getMemcache().getServers());
        } else if (CACHE_NAME_OSCACHE.equals(cacheProperties.getCacheName())) {
            return cacheProperties.getOscache() != null && StringUtils.isNotEmpty(cacheProperties.getOscache().getConfiguration());
        } else {
            return false;
        }
    }

    private String replaceValue(String value) {
        return PropertyPlaceholderHelper.replacePlaceholder(value, false, SystemConfig.getAllConfigs());
    }
}

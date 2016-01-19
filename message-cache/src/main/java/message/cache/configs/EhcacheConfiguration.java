package message.cache.configs;

import message.cache.CacheManager;
import message.cache.ehcache.EHCacheManagerImpl;
import org.springframework.context.annotation.Configuration;

/**
 * Ehcache Configuration.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016/1/19 16:04
 */
@Configuration
public class EhcacheConfiguration extends CacheConfiguration {
    @Override
    protected CacheManager cacheManagerProvider() throws Exception {
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
    }
}

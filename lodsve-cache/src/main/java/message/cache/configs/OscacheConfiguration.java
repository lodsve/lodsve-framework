package message.cache.configs;

import message.cache.CacheManager;
import message.cache.oscache.OsCacheManagerImpl;
import org.springframework.context.annotation.Configuration;

/**
 * Oscache Configuration.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016/1/19 16:05
 */
@Configuration
public class OscacheConfiguration extends CacheConfiguration {
    @Override
    protected CacheManager cacheManagerProvider() throws Exception {
        OsCacheManagerImpl osCacheManager = new OsCacheManagerImpl();

        osCacheManager.setConfiguration(replaceValue(cacheProperties.getOscache().getConfiguration()));
        osCacheManager.setDefaultTimeout(60);
        osCacheManager.setBlocking(false);
        osCacheManager.setMemoryCaching(true);
        osCacheManager.setUnlimitedDiskCache(false);

        return osCacheManager;
    }
}

package message.cache.configs;

import message.cache.CacheManager;
import message.cache.mapcache.MapCacheManagerImpl;
import org.springframework.context.annotation.Configuration;

/**
 * MapCache Configuration.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016/1/19 16:05
 */
@Configuration
public class MapCacheConfiguration extends CacheConfiguration {
    @Override
    protected CacheManager cacheManagerProvider() throws Exception {
        return new MapCacheManagerImpl();
    }
}

package lodsve.cache.ehcache;

import lodsve.cache.properties.CacheProperties;
import lodsve.core.properties.autoconfigure.annotations.EnableConfigurationProperties;
import net.sf.ehcache.Cache;
import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * ehcache.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/4/1 上午9:49
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
public class EhcacheCacheConfiguration {
    @Autowired
    private CacheProperties cacheProperties;

    @Bean
    public CacheManager cacheManager() throws IOException {
        EhCacheCacheManager cacheManager = new EhCacheCacheManager();

        Resource configResource = cacheProperties.getEhcache().getConfiguration();
        if (configResource == null || !configResource.exists()) {
            configResource = new ClassPathResource("/META-INF/ehcache.xml", Thread.currentThread().getContextClassLoader());
        }

        net.sf.ehcache.CacheManager manager = net.sf.ehcache.CacheManager.create(configResource.getInputStream());

        CacheProperties.Ehcache.EhcacheCache[] caches = cacheProperties.getEhcache().getCache();

        for (CacheProperties.Ehcache.EhcacheCache cache : caches) {
            CacheConfiguration configuration = new CacheConfiguration();
            configuration.setName(cache.getName());
            configuration.setMaxEntriesLocalHeap(cache.getMaxElementsInMemory());
            configuration.setEternal(cache.getEternal());
            configuration.setTimeToIdleSeconds(cache.getTimeToIdleSeconds());
            configuration.setTimeToLiveSeconds(cache.getTimeToLiveSeconds());
            configuration.setOverflowToDisk(cache.getOverflowToDisk());

            manager.addCache(new Cache(configuration));
        }

        cacheManager.setCacheManager(manager);
        cacheManager.afterPropertiesSet();

        return cacheManager;
    }
}

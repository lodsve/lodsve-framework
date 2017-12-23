package lodsve.cache.ehcache;

import lodsve.cache.properties.CacheProperties;
import lodsve.core.autoconfigure.annotations.EnableConfigurationProperties;
import net.sf.ehcache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

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
    @Autowired
    private List<Cache> caches;

    @Bean
    public CacheManager cacheManager() throws IOException {
        EhCacheCacheManager cacheManager = new EhCacheCacheManager();

        Resource configResource = cacheProperties.getEhcache().getConfiguration();
        if (configResource == null || !configResource.exists()) {
            configResource = new ClassPathResource("/META-INF/ehcache.xml", Thread.currentThread().getContextClassLoader());
        }

        net.sf.ehcache.CacheManager manager = net.sf.ehcache.CacheManager.create(configResource.getInputStream());
        for (Cache cache : caches) {
            manager.addCache(cache);
        }

        cacheManager.setCacheManager(manager);
        cacheManager.afterPropertiesSet();

        return cacheManager;
    }
}

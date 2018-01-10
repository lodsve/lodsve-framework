package lodsve.cache.oscache;

import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import lodsve.cache.properties.CacheProperties;
import lodsve.core.properties.autoconfigure.annotations.EnableConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 * Oscache Cache Configuration.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2018-1-10-0010 16:52
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
public class OscacheCacheConfiguration {
    @Autowired
    private CacheProperties cacheProperties;

    @Bean
    public CacheManager cacheManager(GeneralCacheAdministrator cacheAdministrator) {
        CacheProperties.Oscahce oscahce = cacheProperties.getOscahce();

        OscacheCacheManager cacheManager = new OscacheCacheManager();
        cacheManager.setAdmin(cacheAdministrator);
        cacheManager.setCacheConfigs(Arrays.asList(oscahce.getCache()));

        return cacheManager;
    }

    @Bean
    public GeneralCacheAdministrator cacheAdministrator() throws IOException {
        CacheProperties.Oscahce oscahce = cacheProperties.getOscahce();
        Resource resource = oscahce.getConfiguration();
        if (resource == null || !resource.exists()) {
            resource = new ClassPathResource("/META-INF/oscache.properties", Thread.currentThread().getContextClassLoader());
        }

        Properties properties = new Properties();
        PropertiesLoaderUtils.fillProperties(properties, resource);

        return new GeneralCacheAdministrator(properties);
    }
}

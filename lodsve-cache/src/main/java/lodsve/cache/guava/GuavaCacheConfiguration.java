package lodsve.cache.guava;

import lodsve.cache.properties.CacheProperties;
import lodsve.core.autoconfigure.annotations.EnableConfigurationProperties;
import lodsve.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * guava.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/4/1 上午9:49
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
public class GuavaCacheConfiguration {
    @Autowired
    private CacheProperties cacheProperties;

    @Bean
    public CacheManager cacheManager() {
        GuavaCacheManager cacheManager = new GuavaCacheManager();

        CacheProperties.Guava guava = cacheProperties.getGuava();
        String cacheNames = guava.getCacheNames();
        List<String> cacheNameList = Arrays.asList(StringUtils.split(cacheNames));

        if (!cacheNameList.isEmpty()) {
            cacheManager.setCacheNames(cacheNameList);
        }

        cacheManager.setCacheSpecification(guava.getSpec());

        return cacheManager;
    }
}

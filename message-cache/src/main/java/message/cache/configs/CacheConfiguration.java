package message.cache.configs;

import message.base.utils.PropertyPlaceholderHelper;
import message.cache.CacheManager;
import message.cache.utils.CacheFactoryBean;
import message.config.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * cache configuration.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/14 下午9:24
 */
@Configuration
public abstract class CacheConfiguration {
    @Autowired
    protected CacheProperties cacheProperties;

    protected abstract CacheManager cacheManagerProvider() throws Exception;

    @Bean
    public CacheManager cacheManager() throws Exception {
        return cacheManagerProvider();
    }

    @Bean
    @Lazy
    public CacheFactoryBean abstractCache() throws Exception {
        CacheFactoryBean factoryBean = new CacheFactoryBean();
        factoryBean.setCacheManager(cacheManager());

        return factoryBean;
    }

    protected String replaceValue(String value) {
        return PropertyPlaceholderHelper.replacePlaceholder(value, false, SystemConfig.getAllConfigs());
    }
}

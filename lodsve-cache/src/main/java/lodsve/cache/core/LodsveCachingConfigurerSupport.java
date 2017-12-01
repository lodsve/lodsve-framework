package lodsve.cache.core;

import lodsve.core.autoconfigure.AutoConfigurationBuilder;
import lodsve.core.autoconfigure.annotations.ConfigurationProperties;
import lodsve.core.config.SystemConfig;
import lodsve.core.utils.PropertyPlaceholderHelper;
import org.springframework.cache.annotation.CachingConfigurerSupport;

import javax.annotation.PostConstruct;

/**
 * 缓存配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/3/31 上午10:12
 */
public class LodsveCachingConfigurerSupport extends CachingConfigurerSupport {
    protected CacheProperties cacheProperties;

    @PostConstruct
    public void init() {
        AutoConfigurationBuilder.Builder<CacheProperties> builder = new AutoConfigurationBuilder.Builder<>();
        builder.setAnnotation(CacheProperties.class.getAnnotation(ConfigurationProperties.class));
        builder.setClazz(CacheProperties.class);

        cacheProperties = builder.build();
    }

    protected String replaceValue(String value) {
        return PropertyPlaceholderHelper.replacePlaceholder(value, false, SystemConfig.getAllConfigs());
    }
}

package lodsve.cache.core;

import lodsve.core.autoconfigure.AutoConfigurationBuilder;
import lodsve.core.autoconfigure.annotations.ConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;

import javax.annotation.PostConstruct;

/**
 * 缓存配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/3/31 上午10:12
 */
public class LodsveCachingConfigurerSupport extends CachingConfigurerSupport {
    CacheProperties cacheProperties;

    @PostConstruct
    public void init() {
        AutoConfigurationBuilder.Builder<CacheProperties> builder = new AutoConfigurationBuilder.Builder<>();
        builder.setAnnotation(CacheProperties.class.getAnnotation(ConfigurationProperties.class));
        builder.setClazz(CacheProperties.class);

        cacheProperties = builder.build();
    }
}

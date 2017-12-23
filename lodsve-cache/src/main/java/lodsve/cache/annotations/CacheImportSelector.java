package lodsve.cache.annotations;

import lodsve.cache.ehcache.EhcacheCacheConfiguration;
import lodsve.cache.guava.GuavaCacheConfiguration;
import lodsve.cache.redis.RedisCacheConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * Cache ImportSelector.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/12/23 下午3:55
 */
public class CacheImportSelector implements ImportSelector {
    private static final String CACHE_MODE_ATTRIBUTE_NAME = "cache";

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableCache.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", EnableCache.class.getName(), importingClassMetadata.getClassName()));

        CacheMode cacheMode = attributes.getEnum(CACHE_MODE_ATTRIBUTE_NAME);

        if (cacheMode == CacheMode.EHCAHE) {
            return new String[]{EhcacheCacheConfiguration.class.getName()};
        } else if (cacheMode == CacheMode.GUAVA) {
            return new String[]{GuavaCacheConfiguration.class.getName()};
        } else if (cacheMode == CacheMode.REDIS) {
            return new String[]{RedisCacheConfiguration.class.getName()};
        }

        return new String[0];
    }
}

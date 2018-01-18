package lodsve.cache.annotations;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * Cache ImportSelector.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2017/12/23 下午3:55
 */
public class CacheImportSelector implements ImportSelector {
    private static final String CACHE_MODE_ATTRIBUTE_NAME = "cache";

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableCache.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", EnableCache.class.getName(), importingClassMetadata.getClassName()));

        CacheMode cacheMode = attributes.getEnum(CACHE_MODE_ATTRIBUTE_NAME);

        return new String[]{cacheMode.getCacheConfig().getName()};
    }
}

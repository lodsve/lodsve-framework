package lodsve.cache.annotations;

import java.util.ArrayList;
import java.util.List;
import lodsve.cache.configs.WithRedisCacheConfiguration;
import lodsve.cache.configs.WithoutRedisCacheConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * 根据{@link EnableCache}中的{@link CacheMode}类型判断启用哪个缓存.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016/1/19 15:10
 * @see EnableCache
 * @see CacheMode
 */
public class CacheConfigurationSelector implements ImportSelector {
    public static final String CACHE_MODE_ATTRIBUTE_NAME = "cache";

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableCache.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", EnableCache.class.getName(), importingClassMetadata.getClassName()));

        List<String> imports = new ArrayList<>();

        CacheMode cacheMode = attributes.getEnum(CACHE_MODE_ATTRIBUTE_NAME);
        CacheModeHolder.setCacheMode(cacheMode);

        if (CacheMode.REDIS == cacheMode) {
            imports.add(WithRedisCacheConfiguration.class.getName());
        } else {
            imports.add(WithoutRedisCacheConfiguration.class.getName());
        }

        return imports.toArray(new String[imports.size()]);
    }
}

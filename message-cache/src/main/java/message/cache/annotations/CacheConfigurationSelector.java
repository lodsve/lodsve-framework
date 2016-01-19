package message.cache.annotations;

import message.cache.configs.*;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据{@link EnableCache}中的{@link CacheMode}类型判断启用哪个缓存.
 *
 * @see EnableCache
 * @see CacheMode
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016/1/19 15:10
 */
public class CacheConfigurationSelector implements ImportSelector {
    public static final String CACHE_MODE_ATTRIBUTE_NAME = "cache";

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableCache.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", EnableCache.class.getName(), importingClassMetadata.getClassName()));

        List<String> imports = new ArrayList<>();

        CacheMode cacheMode = attributes.getEnum(CACHE_MODE_ATTRIBUTE_NAME);
        switch (cacheMode) {
            case EHCAHE:
                imports.add(EhcacheConfiguration.class.getName());
                break;
            case OSCACHE:
                imports.add(OscacheConfiguration.class.getName());
                break;
            case MEMCACHE:
                imports.add(MemcacheConfiguration.class.getName());
                break;
            case MAP:
            default:
                imports.add(MapCacheConfiguration.class.getName());
                break;
        }
        return imports.toArray(new String[imports.size()]);
    }
}

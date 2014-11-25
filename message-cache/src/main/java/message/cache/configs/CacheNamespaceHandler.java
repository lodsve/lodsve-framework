package message.cache.configs;

import message.cache.configs.parser.AbstractCacheConfigParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册缓存配置文件解析器.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-8-29 下午7:59
 */
public class CacheNamespaceHandler extends NamespaceHandlerSupport {
    private static final Logger logger = LoggerFactory.getLogger(CacheNamespaceHandler.class);

    @Override
    public void init() {
        logger.debug("register bean xml parser begin...");
        Map<String, Object> parsers = getAllParserUnderCurrentPackage();
        for (Map.Entry<String, Object> entry : parsers.entrySet()) {
            registerBeanDefinitionParser(entry.getKey(), (BeanDefinitionParser) entry.getValue());
        }
        logger.debug("register bean xml parser end...");
    }

    private Map<String, Object> getAllParserUnderCurrentPackage() {
        try {
            Map<String, Object> parsers = new HashMap<String, Object>();

            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

            String basePackage = this.getClass().getPackage().getName();
            String pattern = "classpath*:" + ClassUtils.convertClassNameToResourcePath(basePackage) + "/parser/*.class";
            Resource[] resources = resourcePatternResolver.getResources(pattern);

            for (Resource r : resources) {
                if (r.isReadable()) {
                    MetadataReader reader = readerFactory.getMetadataReader(r);
                    String classNameWithPackage = reader.getClassMetadata().getClassName();
                    Class clazz = ClassUtils.forName(classNameWithPackage, Thread.currentThread().getContextClassLoader());

                    Class<?> superClass = clazz.getSuperclass();
                    if (AbstractCacheConfigParser.class == superClass && clazz.isAnnotationPresent(CacheType.class)) {
                        parsers.put(((CacheType) clazz.getAnnotation(CacheType.class)).value(), BeanUtils.instantiate(clazz));
                    }
                }
            }

            return parsers;
        } catch (Exception e) {
            throw new CacheException(10009, e, "解析配置错误！");
        }
    }
}

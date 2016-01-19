package message.mybatis.type;

import message.utils.StringUtils;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 扫描所有的MyBatis的TypeHandler.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/6/25 下午7:30
 */
@Component
public class TypeHandlerScanner {
    private static final Logger logger = LoggerFactory.getLogger(TypeHandlerScanner.class);

    private static final String BASE_PACKAGE_SEPARATOR = ",";
    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
    private final List<TypeFilter> includeFilters = new LinkedList<TypeFilter>();

    public TypeHandlerScanner() {
        this.includeFilters.add(new AnnotationTypeFilter(MappedTypes.class));
    }

    /**
     * 通过spring的SpEL扫描系统中配置的枚举类型转换器
     *
     * @param basePackage 枚举类型转换器所在的包路径，可以是多个，以逗号分隔
     * @return
     */
    public TypeHandler<?>[] find(String basePackage) {
        Assert.hasText(basePackage, "base package is required!");
        String[] basePackages = StringUtils.split(basePackage, BASE_PACKAGE_SEPARATOR);
        List<String> typeHandlerClasses = new ArrayList<String>();
        try {
            for (String bp : basePackages) {
                typeHandlerClasses.addAll(this.getTypeHandlerClasses(bp));
            }
        } catch (IOException e) {
            logger.error("扫描mybatis类型转换器发生错误", e);
            return new TypeHandler<?>[0];
        }

        List<TypeHandler<?>> typeHandlers = getTypeHandlers(typeHandlerClasses);

        return typeHandlers.toArray(new TypeHandler<?>[typeHandlers.size()]);
    }

    private List<TypeHandler<?>> getTypeHandlers(List<String> classes) {
        List<TypeHandler<?>> typeHandlers = new ArrayList<TypeHandler<?>>(classes.size());
        for (String typeHandlerClass : classes) {
            TypeHandler<?> handler = getInstance(typeHandlerClass);
            if (handler != null) {
                typeHandlers.add(handler);
            }
        }

        return typeHandlers;
    }

    private TypeHandler<?> getInstance(String typeHandlerClass) {
        Class<TypeHandler<?>> dialectClass;
        try {
            dialectClass = (Class<TypeHandler<?>>) Class.forName(typeHandlerClass);
        } catch (ClassNotFoundException e) {
            logger.error("指定的类型转换器不存在", e);
            return null;
        }

        return BeanUtils.instantiate(dialectClass);
    }

    private List<String> getTypeHandlerClasses(String basePackage) throws IOException {
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                ClassUtils.convertClassNameToResourcePath(basePackage) + "/" + DEFAULT_RESOURCE_PATTERN;

        Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
        List<String> classes = new ArrayList<String>(resources.length);
        for (Resource resource : resources) {
            MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
            if (isMappedTypesClass(metadataReader)) {
                classes.add(metadataReader.getClassMetadata().getClassName());
            }
        }

        return classes;
    }

    private boolean isMappedTypesClass(MetadataReader metadataReader) throws IOException {
        for (TypeFilter tf : this.includeFilters) {
            if (tf.match(metadataReader, this.metadataReaderFactory)) {
                return true;
            }
        }
        return false;
    }
}

package lodsve.mybatis.type;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.LoaderClassPath;
import javassist.Modifier;
import lodsve.core.bean.Codeable;
import lodsve.core.utils.StringUtils;
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
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 扫描所有的MyBatis的TypeHandler.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/6/25 下午7:30
 */
public class TypeHandlerScanner {
    private static final Logger logger = LoggerFactory.getLogger(TypeHandlerScanner.class);

    private static final String BASE_PACKAGE_SEPARATOR = ",";
    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
    private final List<TypeFilter> includeFilters = new LinkedList<>();
    public final ClassPool pool;

    public TypeHandlerScanner() {
        this.includeFilters.add(new AssignableTypeFilter(Codeable.class));
        ClassPool parent = ClassPool.getDefault();
        ClassPool child = new ClassPool(parent);
        child.appendClassPath(new LoaderClassPath(TypeHandlerScanner.class.getClassLoader()));
        child.appendSystemPath();
        child.childFirstLookup = true;
        pool = child;
    }

    /**
     * 通过spring的SpEL扫描系统中配置的枚举类型转换器
     *
     * @param enumPackage 枚举类型所在的包路径，可以是多个，以逗号分隔
     * @return
     */
    public TypeHandler<?>[] find(String enumPackage) {
        Assert.hasText(enumPackage, "enums package is required!");
        String[] basePackages = StringUtils.split(enumPackage, BASE_PACKAGE_SEPARATOR);
        List<String> enumClasses = new ArrayList<>();
        try {
            for (String bp : basePackages) {
                enumClasses.addAll(this.getEnumClasses(bp));
            }
        } catch (IOException e) {
            logger.error("扫描枚举类型发生错误", e);
            return new TypeHandler<?>[0];
        }

        List<TypeHandler<?>> typeHandlers = getTypeHandlers(enumClasses);

        return typeHandlers.toArray(new TypeHandler<?>[typeHandlers.size()]);
    }

    private List<TypeHandler<?>> getTypeHandlers(List<String> classes) {
        List<TypeHandler<?>> typeHandlers = new ArrayList<>(classes.size());
        for (String enumClass : classes) {
            TypeHandler<?> handler = getTypeHandlerInstance(enumClass);
            if (handler != null) {
                typeHandlers.add(handler);
            }
        }

        return typeHandlers;
    }

    private TypeHandler<?> getTypeHandlerInstance(String enumClass) {
        try {
            Class<?> enumClazz = ClassUtils.forName(enumClass, Thread.currentThread().getContextClassLoader());

            //创建一个TypeHandler类
            CtClass typeHandler = pool.makeClass(enumClazz.getSimpleName() + "$TypeHandler$" + System.currentTimeMillis());
            //添加EnumCodeTypeHandler父类(不包含泛型)
            CtClass enumCodeTypeHandler = pool.get(AbstractEnumCodeTypeHandler.class.getName());
            typeHandler.setSuperclass(enumCodeTypeHandler);

            //创建构造器
            CtConstructor constructor = new CtConstructor(null, typeHandler);
            constructor.setModifiers(Modifier.PUBLIC);
            constructor.setBody("{super(" + enumClass + ".class);}");
            typeHandler.addConstructor(constructor);

            // 另取捷径,设置rawType的值
            // mybatis中,最后需要rawType(即枚举的类型)与handler对应
            Class<?> clazz = typeHandler.toClass();
            TypeHandler<?> handler = (TypeHandler<?>) BeanUtils.instantiate(clazz);
            Field field = clazz.getSuperclass().getSuperclass().getSuperclass().getDeclaredField("rawType");
            field.setAccessible(true);
            field.set(handler, enumClazz);

            return handler;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<String> getEnumClasses(String basePackage) throws IOException {
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                ClassUtils.convertClassNameToResourcePath(basePackage) + "/" + DEFAULT_RESOURCE_PATTERN;

        Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
        List<String> classes = new ArrayList<>(resources.length);
        for (Resource resource : resources) {
            MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
            if (isCodeableClass(metadataReader)) {
                classes.add(metadataReader.getClassMetadata().getClassName());
            }
        }

        return classes;
    }

    private boolean isCodeableClass(MetadataReader metadataReader) throws IOException {
        for (TypeFilter tf : this.includeFilters) {
            if (tf.match(metadataReader, this.metadataReaderFactory)) {
                return true;
            }
        }
        return false;
    }
}

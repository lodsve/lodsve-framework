package lodsve.redis.core.annotations;

import lodsve.redis.timer.RedisTimerBeanDefinitionRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * reidis配置注册.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-1-25 16:02
 */
public class RedisImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        List<String> imports = new ArrayList<>();
        imports.add(RedisBeanDefinitionRegistrar.class.getName());
        imports.add(RedisTimerBeanDefinitionRegistrar.class.getName());

        return imports.toArray(new String[imports.size()]);
    }
}

package message.redis.core.annotations;

import message.redis.timer.RedisTimerConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-1-25 16:02
 */
public class RedisImportSelector implements ImportSelector {
    public static final String USE_TIMER_NAME_ATTRIBUTE_NAME = "useTimer";

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        List<String> imports = new ArrayList<>();
        imports.add(RedisBeanDefinitionRegistrar.class.getName());

        AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableRedis.class.getName(), false));
        boolean useTimer = attributes.getBoolean(USE_TIMER_NAME_ATTRIBUTE_NAME);
        if (useTimer) {
            imports.add(RedisTimerConfiguration.class.getName());
        }

        return imports.toArray(new String[imports.size()]);
    }
}

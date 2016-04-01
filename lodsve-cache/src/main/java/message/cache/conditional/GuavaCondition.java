package message.cache.conditional;

import message.cache.annotations.CacheMode;
import message.cache.annotations.CacheModeHolder;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/4/1 上午10:47
 */
public class GuavaCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return CacheMode.GUAVA.equals(CacheModeHolder.getCacheMode());
    }
}

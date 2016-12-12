package lodsve.core.appllication.conditional;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 是否是开发模式.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016/12/12 下午2:59
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnDevelopmentConditional.class)
public @interface ConditionalOnDevelopment {
}

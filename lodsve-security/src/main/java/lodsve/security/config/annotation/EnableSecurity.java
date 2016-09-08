package lodsve.security.config.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用Security.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 16/9/8 上午11:24
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SecurityConfigurationSelector.class)
public @interface EnableSecurity {
}

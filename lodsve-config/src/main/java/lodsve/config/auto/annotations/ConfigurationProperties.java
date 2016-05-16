package lodsve.config.auto.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/12/30 上午9:28
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface ConfigurationProperties {
    /**
     * The name prefix of the properties that are valid to bind to this object.
     *
     * @return the name prefix of the properties to bind
     */
    String prefix() default "";

    /**
     * Optionally provide explicit resource locations to bind to. By default the
     * configuration at these specified locations will be merged with the default
     * configuration.
     *
     * @return the path (or paths) of resources to bind to
     */
    String[] locations() default {};
}

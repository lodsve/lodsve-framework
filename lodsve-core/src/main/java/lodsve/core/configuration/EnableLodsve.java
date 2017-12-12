package lodsve.core.configuration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enable Lodsve.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2017/12/12 12:53
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LodsveExtConfigurationLoader.class)
public @interface EnableLodsve {
}

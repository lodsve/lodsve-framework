package lodsve.core.configuration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

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

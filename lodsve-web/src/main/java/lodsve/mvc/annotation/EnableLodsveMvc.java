package lodsve.mvc.annotation;

import lodsve.core.configuration.EnableLodsve;
import lodsve.mvc.config.WebMvcConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用lodsve-mvc.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/18 下午10:50
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
@EnableLodsve
@EnableWebMvc
@Import({WebMvcConfiguration.class})
public @interface EnableLodsveMvc {
}

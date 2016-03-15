package message.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import message.mvc.config.WebMvcConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * 启用cosmos-mvc.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/18 下午10:50
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
@EnableWebMvc
@Import({WebMvcConfiguration.class})
public @interface EnableCosmosMvc {
}

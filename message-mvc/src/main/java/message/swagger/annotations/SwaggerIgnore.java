package message.swagger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 忽略加上这个注解的controller.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/26 下午10:36
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SwaggerIgnore {
}

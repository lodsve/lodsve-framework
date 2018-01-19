package lodsve.security.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 鉴权:需要认证(authentication).
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0
 * @createTime 2014-12-7 14:09
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authn {
}

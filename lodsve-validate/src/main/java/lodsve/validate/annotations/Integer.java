package lodsve.validate.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 验证框架:整数限制注解.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-23 下午8:41
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Integer {
    /**
     * 最小值
     *
     * @return
     */
    int min() default 0;

    /**
     * 最大值
     * @return
     */
    int max() default 0;
}

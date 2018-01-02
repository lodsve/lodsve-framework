package lodsve.test.base;

import java.lang.annotation.*;

/**
 * 设置单元测试的params.home.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2017-12-28-0028 16:52
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Inherited
public @interface ParamsPath {
    /**
     * params.home
     *
     * @return params.home
     */
    String value() default "";
}

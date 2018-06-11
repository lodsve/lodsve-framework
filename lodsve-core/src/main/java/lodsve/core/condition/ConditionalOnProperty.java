package lodsve.core.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * conditional property是否匹配给定的值.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016/12/8 下午5:55
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnPropertyCondition.class)
public @interface ConditionalOnProperty {
    /**
     * relaxed bind class name
     *
     * @return relaxed bind class name
     */
    Class<?> clazz() default Object.class;

    /**
     * properties的key
     *
     * @return key
     */
    String key();

    /**
     * properties的value
     *
     * @return value
     */
    String value();
}

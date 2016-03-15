package message.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 当前台传入类似于1,2,3,4,5之类的字符串，加上此注解，将会被解析成一个List，这个注解是一些配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2015-2-9 18:58
 * @see message.mvc.resolver.ParseDataHandlerMethodArgumentResolver
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Parse {
    /**
     * 参数值在request中key，默认为参数名
     *
     * @return
     */
    String value() default "";

    /**
     * list中需要的泛型
     *
     * @return
     */
    Class<?> dest();

    /**
     * 是否必填
     *
     * @return
     */
    boolean required() default false;

    /**
     * 分隔符，默认为[,]
     *
     * @return
     */
    String delimiters() default ",";
}

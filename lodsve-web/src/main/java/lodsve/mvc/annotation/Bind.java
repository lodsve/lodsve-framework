package lodsve.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表单提交多个对象，且这些对象含有同名的字段，需要用此注解标注参数.<br/>
 * 解析类为{@link lodsve.mvc.resolver.BindDataHandlerMethodArgumentResolver}
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2015-1-29 21:45
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bind {
    /**
     * 表单提交时，字段前缀
     *
     * @return
     */
    String value();
}

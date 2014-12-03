package message.validate.exception;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 异常处理类,如果要实现自定义异常,则全部实现这个接口.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-12-2 上午1:08
 */
public interface ExceptionHandler {

    /**
     * 处理异常的方法
     *
     * @param validateClass             验证的类
     * @param validateField             验证的字段
     * @param value                     这个字段的输入值
     * @param annotation                这个字段上的注解
     * @throws Exception
     */
    void doHandleException(Class<?> validateClass, Field validateField, Object value, Annotation annotation) throws Exception;
}

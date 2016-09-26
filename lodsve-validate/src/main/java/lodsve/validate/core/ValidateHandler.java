package lodsve.validate.core;

import lodsve.validate.constants.ValidateConstants;
import lodsve.validate.exception.ErrorMessage;
import lodsve.validate.handler.DoubleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

/**
 * 所有验证条件的处理类必须继承这个抽象类,以实现各自的验证方法.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午7:46
 */
public abstract class ValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(DoubleHandler.class);

    /**
     * 所有继承这个类的类才会是对应注解验证类型的验证类,验证的时候都调用这个方法
     *
     * @param annotation 待验证字段的注解
     * @param value      待验证字段的值
     * @return
     */
    protected abstract ErrorMessage handle(Annotation annotation, Object value);

    /**
     * 在抽象类中进行每个验证组件都会执行的操作(判空)
     *
     * @param annotation 待验证字段的注解
     * @param value      待验证字段的值
     * @return
     */
    public ErrorMessage validate(Annotation annotation, Object value) {
        if (annotation == null || value == null) {
            logger.error("given null annotation! or null value!");
            return null;
        }

        return this.handle(annotation, value);
    }

    protected ErrorMessage getMessage(Class<? extends Annotation> annotation, Class<? extends ValidateHandler> handler, String key, boolean result, Object... args) {
        if (result) {
            return null;
        }

        return new ErrorMessage(annotation, handler, ValidateConstants.getMessage(key, args));
    }
}

package lodsve.validate.core;

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
    protected abstract boolean handle(Annotation annotation, Object value);

    /**
     * 在抽象类中进行每个验证组件都会执行的操作(判空)
     *
     * @param annotation    待验证字段的注解
     * @param value         待验证字段的值
     * @return
     */
    public boolean validate(Annotation annotation, Object value){
        if (annotation == null || value == null) {
            logger.error("given null annotation! or null value!");
            return false;
        }

        return this.handle(annotation, value);
    }
}

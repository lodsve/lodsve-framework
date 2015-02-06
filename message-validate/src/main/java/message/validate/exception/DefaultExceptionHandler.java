package message.validate.exception;

import message.utils.PropertyPlaceholderHelper;
import message.validate.constants.ValidateConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 默认的异常处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-12-2 上午1:11
 */
public class DefaultExceptionHandler implements ExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    public void doHandleException(Class<?> validateClass, Field validateField, Object value, Annotation annotation) throws Exception {
        Exception ex = new DataValidateException(10011, ValidateConstants.VALIDATE_NO, validateClass.getName(),
                validateField.getName(), value.toString(), annotation.annotationType().getSimpleName());
        if (logger.isErrorEnabled())
            logger.error(PropertyPlaceholderHelper.replacePlaceholder(ValidateConstants.VALIDATE_NO, new Object[]{
                    validateClass.getName(), validateField.getName(), value, annotation.annotationType().getSimpleName()
            }), ex);

        throw ex;
    }
}

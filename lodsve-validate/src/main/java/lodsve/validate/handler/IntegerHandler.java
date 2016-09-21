package lodsve.validate.handler;

import lodsve.core.utils.NumberUtils;
import lodsve.core.utils.ValidateUtils;
import lodsve.validate.annotations.Integer;
import lodsve.validate.core.ValidateHandler;
import lodsve.validate.exception.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * 整数验证的处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午8:28
 */
public class IntegerHandler extends ValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(IntegerHandler.class);

    public IntegerHandler() throws IOException {
        super();
    }

    protected ErrorMessage handle(Annotation annotation, Object value) {
        if (logger.isDebugEnabled())
            logger.debug("annotation is '{}', value is '{}'!", annotation, value);

        if (!NumberUtils.isNumber(value + "")) {
            logger.error("it is not Integer, '{}'!", value);
            return getMessage(Integer.class, getClass(), "integer-invalid", false);
        }

        lodsve.validate.annotations.Integer i = (lodsve.validate.annotations.Integer) annotation;
        boolean result;
        int min = i.min();
        int max = i.max();

        if (min < max) {
            int v = java.lang.Integer.valueOf(value + "");
            logger.debug("get validate min is '{}', max is '{}', value is '{}'", new int[]{min, max, v});
            result = v >= min && v <= max && ValidateUtils.isInteger(value + "");
            return getMessage(Integer.class, this.getClass(), "integer-length", result, min, max);
        }

        return null;
    }
}

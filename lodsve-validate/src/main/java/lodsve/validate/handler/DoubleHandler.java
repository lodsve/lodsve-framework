package lodsve.validate.handler;

import lodsve.core.utils.ValidateUtils;
import lodsve.validate.annotations.Double;
import lodsve.validate.core.ValidateHandler;
import lodsve.validate.exception.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * 小数验证的处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午8:13
 */
public class DoubleHandler extends ValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(DoubleHandler.class);

    public DoubleHandler() throws IOException {
        super();
    }

    public ErrorMessage handle(Annotation annotation, Object value) {
        logger.debug("annotation is '{}', value is '{}'!", annotation, value);

        return getMessage(Double.class, getClass(), "double-error", ValidateUtils.isDouble(value + ""));
    }
}

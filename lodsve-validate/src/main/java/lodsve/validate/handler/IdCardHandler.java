package lodsve.validate.handler;

import lodsve.core.utils.ValidateUtils;
import lodsve.validate.annotations.IdCard;
import lodsve.validate.core.ValidateHandler;
import lodsve.validate.exception.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * 身份证号码验证的处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午8:26
 */
public class IdCardHandler extends ValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(IdCardHandler.class);

    public IdCardHandler() throws IOException {
        super();
    }

    protected ErrorMessage handle(Annotation annotation, Object value) {
        if (logger.isDebugEnabled())
            logger.debug("annotation is '{}', value is '{}'!", annotation, value);

        return getMessage(IdCard.class, getClass(), "id-card-error", ValidateUtils.isIdCard((String) value));
    }
}

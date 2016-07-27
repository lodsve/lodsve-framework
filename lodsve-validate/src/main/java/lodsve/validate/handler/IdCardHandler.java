package lodsve.validate.handler;

import lodsve.core.utils.ValidateUtils;
import lodsve.validate.core.ValidateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    protected boolean handle(Annotation annotation, Object value) {
        if (logger.isDebugEnabled())
            logger.debug("annotation is '{}', value is '{}'!", annotation, value);

        return ValidateUtils.isIdCard((String) value);
    }
}

package lodsve.validate.handler;

import lodsve.core.utils.ValidateUtils;
import lodsve.validate.core.ValidateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

/**
 * qq号码验证处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午9:35
 */
public class QqHandler extends ValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(QqHandler.class);

    protected boolean handle(Annotation annotation, Object value) {
        if(logger.isDebugEnabled())
            logger.debug("annotation is '{}', value is '{}'!", annotation, value);

        return ValidateUtils.isQq((String) value);
    }
}

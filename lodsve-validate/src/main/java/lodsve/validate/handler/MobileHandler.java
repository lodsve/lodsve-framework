package lodsve.validate.handler;

import lodsve.base.utils.ValidateUtils;
import lodsve.validate.core.ValidateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

/**
 * 手机号码验证处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午8:49
 */
public class MobileHandler extends ValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(MobileHandler.class);

    protected boolean handle(Annotation annotation, Object value) {
        if(logger.isDebugEnabled())
            logger.debug("annotation is '{}', value is '{}'!", annotation, value);

        return ValidateUtils.isMobile((String) value);
    }
}

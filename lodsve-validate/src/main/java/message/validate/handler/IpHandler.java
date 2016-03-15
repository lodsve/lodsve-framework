package message.validate.handler;

import message.base.utils.ValidateUtils;
import message.validate.core.ValidateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

/**
 * IP的验证处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午8:36
 */
public class IpHandler extends ValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(IpHandler.class);

    protected boolean handle(Annotation annotation, Object value) {
        if(logger.isDebugEnabled())
            logger.debug("annotation is '{}', value is '{}'!", annotation, value);

        return ValidateUtils.isIp((String) value);
    }
}

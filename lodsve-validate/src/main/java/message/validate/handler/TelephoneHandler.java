package message.validate.handler;

import message.base.utils.ValidateUtils;
import message.validate.core.ValidateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

/**
 * 电话号码验证处理类(固话).
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午9:47
 */
public class TelephoneHandler extends ValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(TelephoneHandler.class);

    protected boolean handle(Annotation annotation, Object value) {
        if(logger.isDebugEnabled())
            logger.debug("annotation is '{}', value is '{}'!", annotation, value);

        return ValidateUtils.isPhone((String) value);
    }
}

package message.validate.handler;

import message.base.utils.ObjectUtils;
import message.validate.core.ValidateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

/**
 * 不为空验证处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午8:51
 */
public class NotNullHandler extends ValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(NotNullHandler.class);

    protected boolean handle(Annotation annotation, Object value) {
        try {
            return ObjectUtils.isNotEmpty(value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }
}

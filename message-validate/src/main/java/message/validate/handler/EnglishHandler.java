package message.validate.handler;

import message.base.utils.ValidateUtils;
import message.validate.annotations.English;
import message.validate.core.ValidateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

/**
 * 英文验证的处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午8:24
 */
public class EnglishHandler extends ValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(EnglishHandler.class);

    protected boolean handle(Annotation annotation, Object value) {
        English c = (English) annotation;
        int min = c.min();
        int max = c.max();

        if (min >= max) {
            logger.debug("no length limit!");
            return ValidateUtils.isEnglish((String) value);
        } else {
            int length = ((String) value).length();
            logger.debug("get validate min is '{}', max is '{}', value length is '{}'", new int[]{min, max, length});
            return min <= length && max >= length && ValidateUtils.isEnglish((String) value);
        }
    }
}

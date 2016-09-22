package lodsve.validate.handler;

import lodsve.core.utils.ValidateUtils;
import lodsve.validate.annotations.Chinese;
import lodsve.validate.core.ValidateHandler;
import lodsve.validate.exception.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;

import static lodsve.core.utils.ValidateUtils.isChinese;

/**
 * 中文验证的处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午7:48
 */
public class ChineseHandler extends ValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(ChineseHandler.class);

    public ChineseHandler() throws IOException {
        super();
    }

    public ErrorMessage handle(Annotation annotation, Object value) {
        Chinese c = (Chinese) annotation;
        int min = c.min();
        int max = c.max();

        boolean result;
        if (min >= max) {
            logger.debug("no length limit!");
            result = ValidateUtils.isChinese((String) value);
            return getMessage(Chinese.class, this.getClass(), "chinese-invalid", result);
        } else {
            int length = ((String) value).length();
            logger.debug("get validate min is '{}', max is '{}', value length is '{}'", min, max, length);
            result = min <= length && max >= length && isChinese((String) value);
            return getMessage(Chinese.class, this.getClass(), "chinese-length", result, min, max, length);
        }
    }
}

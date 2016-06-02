package lodsve.validate.handler;

import lodsve.core.utils.ValidateUtils;
import lodsve.validate.annotations.Chinese;
import lodsve.validate.core.ValidateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

/**
 * 中文验证的处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午7:48
 */
public class ChineseHandler extends ValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(ChineseHandler.class);

    public boolean handle(Annotation annotation, Object value) {
        Chinese c = (Chinese) annotation;
        int min = c.min();
        int max = c.max();

        if(min >= max){
            logger.debug("no length limit!");
            return ValidateUtils.isChinese((String) value);
        } else {
            int length = ((String) value).length();
            logger.debug("get validate min is '{}', max is '{}', value length is '{}'", new int[]{min, max, length});
            return min <= length && max >= length && ValidateUtils.isChinese((String) value);
        }
    }
}

package message.validate.handler;

import message.base.utils.NumberUtils;
import message.base.utils.ValidateUtils;
import message.validate.core.ValidateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

/**
 * 整数验证的处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午8:28
 */
public class IntegerHandler extends ValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(IntegerHandler.class);

    protected boolean handle(Annotation annotation, Object value) {
        if(logger.isDebugEnabled())
            logger.debug("annotation is '{}', value is '{}'!", annotation, value);

        if(!NumberUtils.isNumber(value + "")){
            logger.error("it is not Integer, '{}'!", value);
            return false;
        }

        message.validate.annotations.Integer i = (message.validate.annotations.Integer) annotation;
        int min = i.min();
        int max = i.max();
        if(min >= max){
            logger.debug("no any limit!");
            return ValidateUtils.isInteger(value + "");
        } else {
            Integer v = Integer.valueOf(value + "");
            logger.debug("get validate min is '{}', max is '{}', value is '{}'", new int[]{min, max, v});
            return v >= min && v <= max && ValidateUtils.isInteger(value + "");
        }
    }
}

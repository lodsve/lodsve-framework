package message.validate.handler;

import message.validate.annotations.Limit;
import message.validate.core.ValidateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * 长度验证处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午8:39
 */
public class LimitHandler extends ValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(LimitHandler.class);

    protected boolean handle(Annotation annotation, Object value) {
        int length = 0;
        if(value instanceof String){
            //字符串
            length = ((String) value).length();
        } else if(value instanceof Map){
            //map
            length = ((Map) value).size();
        } else if(value instanceof Collection){
            //集合
            length = ((Collection) value).size();
        } else if(value.getClass().isArray()){
            //数组
            length = Array.getLength(value);
        }

        if(logger.isDebugEnabled())
            logger.debug("value's type is '{}', its length is '{}'", value.getClass().getCanonicalName(), length);

        Limit limit = (Limit) annotation;
        int min = limit.min();
        int max = limit.max();
        if(min < max){
            return min <= length && length <= max;
        }

        return true;
    }
}

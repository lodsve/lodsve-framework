package message.validate.handler;

import message.utils.StringUtils;
import message.validate.annotations.Password;
import message.validate.core.ValidateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 密码验证处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午8:54
 */
public class PasswordHandler extends ValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(PasswordHandler.class);

    protected boolean handle(Annotation annotation, Object value) {
        if(!(value instanceof String)){
            logger.error("is not string!");
            return false;
        }
        Password password = (Password) annotation;
        String regex = password.regex();
        int min = password.min();
        int max = password.max();
        int length = ((String) value).length();

        if(StringUtils.isEmpty(regex)) {
            //不通过正则表达式
            return min < max && min <= length && max >= length;
        } else {
            //通过正则表达式验证
            Pattern patter = Pattern.compile(regex);
            Matcher matcher = patter.matcher((String) value);
            boolean l = true;
            if(min < max)
                //并且有长度的校验
                l = (min <= length && max >= length);

            return matcher.matches() && l;
        }
    }
}

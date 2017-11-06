package lodsve.validate.handler;

import lodsve.core.utils.StringUtils;
import lodsve.validate.annotations.Regex;
import lodsve.validate.core.AbstractValidateHandler;
import lodsve.validate.exception.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式验证处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午9:37
 */
public class RegexHandler extends AbstractValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(RegexHandler.class);

    public RegexHandler() throws IOException {
        super();
    }

    @Override
    protected ErrorMessage handle(Annotation annotation, Object value) {
        if (!(value instanceof String)) {
            logger.error("is not string!");
            return getMessage(Regex.class, getClass(), "regex-error", false);
        }
        Regex regex = (Regex) annotation;
        String regexPattern = regex.regex();
        if (StringUtils.isEmpty(regexPattern)) {
            logger.error("given empty regex string!");
            throw new RuntimeException("regex pattern string is null!");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("the regex pattern is '{}'!", regexPattern);
        }

        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher((String) value);

        return getMessage(Regex.class, getClass(), "regex-not-match", matcher.matches(), regex, value);
    }
}

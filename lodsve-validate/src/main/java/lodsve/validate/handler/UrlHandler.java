package lodsve.validate.handler;

import lodsve.core.utils.ValidateUtils;
import lodsve.validate.annotations.Url;
import lodsve.validate.core.ValidateHandler;
import lodsve.validate.exception.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * url验证处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午9:49
 */
public class UrlHandler extends ValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(UrlHandler.class);

    public UrlHandler() throws IOException {
        super();
    }

    protected ErrorMessage handle(Annotation annotation, Object value) {
        if (logger.isDebugEnabled())
            logger.debug("annotation is '{}', value is '{}'!", annotation, value);

        return getMessage(Url.class, getClass(), "url-invalid", ValidateUtils.isUrl((String) value));
    }
}

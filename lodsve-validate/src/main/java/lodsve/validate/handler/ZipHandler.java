package lodsve.validate.handler;

import lodsve.core.utils.ValidateUtils;
import lodsve.validate.annotations.Zip;
import lodsve.validate.core.ValidateHandler;
import lodsve.validate.exception.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * 邮政编码的验证处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午9:50
 */
public class ZipHandler extends ValidateHandler {
    private static final Logger logger = LoggerFactory.getLogger(ZipHandler.class);

    public ZipHandler() throws IOException {
        super();
    }

    protected ErrorMessage handle(Annotation annotation, Object value) {
        if (logger.isDebugEnabled())
            logger.debug("annotation is '{}', value is '{}'!", annotation, value);

        return getMessage(Zip.class, getClass(), "zip-invalid", ValidateUtils.isZip((String) value));
    }
}

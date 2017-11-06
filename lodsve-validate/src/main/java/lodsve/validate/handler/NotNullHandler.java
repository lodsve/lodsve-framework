package lodsve.validate.handler;

import lodsve.core.utils.ObjectUtils;
import lodsve.validate.annotations.NotNull;
import lodsve.validate.core.AbstractValidateHandler;
import lodsve.validate.exception.ErrorMessage;

import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * 不为空验证处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午8:51
 */
public class NotNullHandler extends AbstractValidateHandler {
    public NotNullHandler() throws IOException {
        super();
    }

    @Override
    protected ErrorMessage handle(Annotation annotation, Object value) {
        try {
            return getMessage(NotNull.class, getClass(), "not-null-error", ObjectUtils.isNotEmpty(value));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

package lodsve.validate.exception;

import lodsve.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认的异常处理类.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0
 * @createTime 12-12-2 上午1:11
 */
public class DefaultExceptionHandler extends ExceptionHandler {

    @Override
    public String getMessage(List<ErrorMessage> messages) {
        List<String> msgs = new ArrayList<>(messages.size());
        for (ErrorMessage em : messages) {
            msgs.add(em.getMessage());
        }

        return StringUtils.join(msgs, "\r\n");
    }
}

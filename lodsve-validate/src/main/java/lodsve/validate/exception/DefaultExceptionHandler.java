package lodsve.validate.exception;

import lodsve.core.utils.StringUtils;
import lodsve.validate.constants.ValidateConstants;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认的异常处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-12-2 上午1:11
 */
public class DefaultExceptionHandler implements ExceptionHandler {
    @Override
    public void doHandleException(List<ErrorMessage> messages) throws Exception {
        if (CollectionUtils.isEmpty(messages)) {
            return;
        }

        List<String> msgs = new ArrayList<>(messages.size());
        for (ErrorMessage em : messages) {
            msgs.add(em.getMessage());
        }

        StringBuilder sb = new StringBuilder(ValidateConstants.getMessage("error-occurred"));
        sb.append("\r\n").append(StringUtils.join(msgs, "\r\n"));

        throw new Exception(sb.toString());
    }
}

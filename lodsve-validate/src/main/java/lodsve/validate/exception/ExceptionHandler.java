package lodsve.validate.exception;

import lodsve.validate.constants.ValidateConstants;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * 异常处理类,如果要实现自定义异常,则全部继承这个类.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0
 * @createTime 12-12-2 上午1:08
 */
public abstract class ExceptionHandler {

    /**
     * 处理异常的方法
     *
     * @param messages 检验结果
     * @throws Exception
     */
    public final void doHandleException(List<ErrorMessage> messages) throws IllegalValidateException {
        if (CollectionUtils.isEmpty(messages)) {
            return;
        }

        throw new IllegalValidateException(ValidateConstants.getMessage("error-occurred") + "\r\n" + getMessage(messages));
    }

    public abstract String getMessage(List<ErrorMessage> messages);
}

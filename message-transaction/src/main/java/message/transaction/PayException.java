package message.transaction;

import message.base.exception.ApplicationRuntimeException;

/**
 * 支付异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/8/20 上午11:15
 */
public class PayException extends ApplicationRuntimeException {

    public PayException(String content) {
        super(content);
    }

    public PayException(Integer code, String content) {
        super(code, content);
    }

    public PayException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}

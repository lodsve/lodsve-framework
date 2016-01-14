package message.transaction;

import message.base.exception.ApplicationRuntimeException;

/**
 * 退款异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/8/20 上午11:15
 */
public class RefundException extends ApplicationRuntimeException {
    public RefundException(String content) {
        super(content);
    }

    public RefundException(Integer code, String content) {
        super(code, content);
    }

    public RefundException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}

package message.transaction;

import message.base.exception.ApplicationRuntimeException;

/**
 * 退款异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/8/20 上午11:15
 */
public class RefundException extends ApplicationRuntimeException {

    public RefundException(int errorCode) {
        super(errorCode);
    }

    public RefundException(int errorCode, Throwable exception, String message, String... args) {
        super(errorCode, exception, message, args);
    }

    public RefundException(int errorCode, Throwable exception) {
        super(errorCode, exception);
    }

    public RefundException(int errorCode, String message, String... args) {
        super(errorCode, message, args);
    }
}

package message.transaction;

import message.base.exception.ApplicationRuntimeException;

/**
 * 支付异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/8/20 上午11:15
 */
public class PayException extends ApplicationRuntimeException {

    public PayException(int errorCode) {
        super(errorCode);
    }

    public PayException(int errorCode, Throwable exception, String message, String... args) {
        super(errorCode, exception, message, args);
    }

    public PayException(int errorCode, Throwable exception) {
        super(errorCode, exception);
    }

    public PayException(int errorCode, String message, String... args) {
        super(errorCode, message, args);
    }
}

package message.transaction;

import message.base.exception.ApplicationRuntimeException;

/**
 * 无效的ping++回调.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/10/27 上午2:15
 */
public class InvalidWebhooksException extends ApplicationRuntimeException {
    public InvalidWebhooksException(int errorCode) {
        super(errorCode);
    }

    public InvalidWebhooksException(int errorCode, Throwable exception, String message, String... args) {
        super(errorCode, exception, message, args);
    }

    public InvalidWebhooksException(int errorCode, Throwable exception) {
        super(errorCode, exception);
    }

    public InvalidWebhooksException(int errorCode, String message, String... args) {
        super(errorCode, message, args);
    }
}

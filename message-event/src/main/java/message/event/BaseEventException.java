package message.event;

import message.base.exception.ApplicationRuntimeException;

/**
 * 事件的定义异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 13-4-27 上午5:45
 */
public class BaseEventException extends ApplicationRuntimeException {
    public BaseEventException(int errorCode) {
        super(errorCode);
    }

    public BaseEventException(int errorCode, Throwable exception, String message, String... args) {
        super(errorCode, exception, message, args);
    }

    public BaseEventException(int errorCode, Throwable exception) {
        super(errorCode, exception);
    }

    public BaseEventException(int errorCode, String message, String... args) {
        super(errorCode, message, args);
    }
}

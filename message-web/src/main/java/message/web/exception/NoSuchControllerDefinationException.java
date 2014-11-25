package message.web.exception;

import message.base.exception.ApplicationRuntimeException;

/**
 * controller没有定义的自定义异常.
 * 
 * @author Danny(sunhao)(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-10-12 下午08:19:00
 */
public class NoSuchControllerDefinationException extends ApplicationRuntimeException {
    public NoSuchControllerDefinationException(int errorCode) {
        super(errorCode);
    }

    public NoSuchControllerDefinationException(int errorCode, Throwable exception, String message, String... args) {
        super(errorCode, exception, message, args);
    }

    public NoSuchControllerDefinationException(int errorCode, Throwable exception) {
        super(errorCode, exception);
    }

    public NoSuchControllerDefinationException(int errorCode, String message, String... args) {
        super(errorCode, message, args);
    }
}

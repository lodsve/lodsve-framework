package message.security.exception;

import message.base.exception.ApplicationRuntimeException;

/**
 * 用户已存在的异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-5 20:43
 */
public class SecurityException extends ApplicationRuntimeException {
    public SecurityException(int errorCode) {
        super(errorCode);
    }

    public SecurityException(int errorCode, Throwable exception, String message, String... args) {
        super(errorCode, exception, message, args);
    }

    public SecurityException(int errorCode, Throwable exception) {
        super(errorCode, exception);
    }

    public SecurityException(int errorCode, String message, String... args) {
        super(errorCode, message, args);
    }
}

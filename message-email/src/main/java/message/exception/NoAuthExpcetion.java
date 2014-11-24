package message.exception;

import message.base.exception.ApplicationRuntimeException;

/**
 * 认证信息错误的异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 13-3-25 上午8:46
 */
public class NoAuthExpcetion extends ApplicationRuntimeException {

    public NoAuthExpcetion(int errorCode) {
        super(errorCode);
    }

    public NoAuthExpcetion(int errorCode, Throwable exception, String message, String... args) {
        super(errorCode, exception, message, args);
    }

    public NoAuthExpcetion(int errorCode, Throwable exception) {
        super(errorCode, exception);
    }

    public NoAuthExpcetion(int errorCode, String message, String... args) {
        super(errorCode, message, args);
    }
}

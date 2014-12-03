package message.validate.exception;

import message.base.exception.ApplicationRuntimeException;

/**
 * 验证没有通过的异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-12-2 上午12:58
 */
public class DataValidateException extends ApplicationRuntimeException {
    private static final long serialVersionUID = -6676669806465518670L;

    public DataValidateException(int errorCode) {
        super(errorCode);
    }

    public DataValidateException(int errorCode, Throwable exception, String message, String... args) {
        super(errorCode, exception, message, args);
    }

    public DataValidateException(int errorCode, Throwable exception) {
        super(errorCode, exception);
    }

    public DataValidateException(int errorCode, String message, String... args) {
        super(errorCode, message, args);
    }
}

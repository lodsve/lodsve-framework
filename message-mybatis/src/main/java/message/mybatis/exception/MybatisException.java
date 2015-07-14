package message.mybatis.exception;

import message.base.exception.ApplicationRuntimeException;

/**
 * mybatis异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/7/14 下午2:20
 */
public class MybatisException extends ApplicationRuntimeException {
    public MybatisException(int errorCode) {
        super(errorCode);
    }

    public MybatisException(int errorCode, Throwable exception, String message, String... args) {
        super(errorCode, exception, message, args);
    }

    public MybatisException(int errorCode, Throwable exception) {
        super(errorCode, exception);
    }

    public MybatisException(int errorCode, String message, String... args) {
        super(errorCode, message, args);
    }
}

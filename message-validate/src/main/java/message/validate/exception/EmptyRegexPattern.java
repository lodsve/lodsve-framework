package message.validate.exception;

import message.base.exception.ApplicationRuntimeException;

/**
 * 正则表达式的验证字符为空的异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午9:43
 */
public class EmptyRegexPattern extends ApplicationRuntimeException {
    private static final long serialVersionUID = -6676669806465518670L;


    public EmptyRegexPattern(int errorCode) {
        super(errorCode);
    }

    public EmptyRegexPattern(int errorCode, Throwable exception, String message, String... args) {
        super(errorCode, exception, message, args);
    }

    public EmptyRegexPattern(int errorCode, Throwable exception) {
        super(errorCode, exception);
    }

    public EmptyRegexPattern(int errorCode, String message, String... args) {
        super(errorCode, message, args);
    }
}

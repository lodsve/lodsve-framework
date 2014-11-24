package message.base.exception;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * 系统异常类，各模块异常类都是继承于此类
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-2-24 下午10:30:49
 */
public class ApplicationRuntimeException extends NestableRuntimeException {
    private static final long serialVersionUID = 1843977652827496719L;

    /**
     * 错误代码
     */
    private int errorCode;
    /**
     * 异常
     */
    private Throwable exception;
    /**
     * 异常描述
     */
    private String message;
    /**
     * 参数
     */
    private String[] args;

    /**
     * 构造器
     *
     * @param errorCode 错误代码
     */
    public ApplicationRuntimeException(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 构造器
     *
     * @param errorCode 错误代码
     * @param exception 异常
     * @param message   异常描述
     * @param args      参数
     */
    public ApplicationRuntimeException(int errorCode, Throwable exception, String message, String... args) {
        this.errorCode = errorCode;
        this.exception = exception;
        this.message = message;
        this.args = args;
    }

    /**
     * 构造器
     *
     * @param errorCode 错误代码
     * @param exception 异常
     */
    public ApplicationRuntimeException(int errorCode, Throwable exception) {
        this.errorCode = errorCode;
        this.exception = exception;
    }

    /**
     * 构造器
     *
     * @param errorCode 错误代码
     * @param message   异常描述
     * @param args      参数
     */
    public ApplicationRuntimeException(int errorCode, String message, String... args) {
        this.errorCode = errorCode;
        this.message = message;
        this.args = args;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getException() {
        return exception;
    }

    public String[] getArgs() {
        return args;
    }
}

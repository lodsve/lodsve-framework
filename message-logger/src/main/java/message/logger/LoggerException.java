package message.logger;

import message.base.exception.ApplicationRuntimeException;

/**
 * 日志配置文件异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-8-24 下午8:57
 */
public class LoggerException extends ApplicationRuntimeException {

    public LoggerException(int errorCode) {
        super(errorCode);
    }

    public LoggerException(int errorCode, Throwable exception, String message, String... args) {
        super(errorCode, exception, message, args);
    }

    public LoggerException(int errorCode, Throwable exception) {
        super(errorCode, exception);
    }

    public LoggerException(int errorCode, String message, String... args) {
        super(errorCode, message, args);
    }
}

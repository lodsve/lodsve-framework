package message.config.exception;

import message.base.exception.ApplicationRuntimeException;

/**
 * 配置文件异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-8-17 下午9:55
 */
public class ConfigException extends ApplicationRuntimeException {

    public ConfigException(int errorCode) {
        super(errorCode);
    }

    public ConfigException(int errorCode, Throwable exception, String message, String... args) {
        super(errorCode, exception, message, args);
    }

    public ConfigException(int errorCode, Throwable exception) {
        super(errorCode, exception);
    }

    public ConfigException(int errorCode, String message, String... args) {
        super(errorCode, message, args);
    }
}

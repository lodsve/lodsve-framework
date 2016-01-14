package message.logger;

import message.base.exception.ApplicationRuntimeException;

/**
 * 日志配置文件异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-8-24 下午8:57
 */
public class LoggerException extends ApplicationRuntimeException {

    public LoggerException(String content) {
        super(content);
    }

    public LoggerException(Integer code, String content) {
        super(code, content);
    }

    public LoggerException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}

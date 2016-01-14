package message.config.exception;

import message.base.exception.ApplicationRuntimeException;

/**
 * 配置文件异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-8-17 下午9:55
 */
public class ConfigException extends ApplicationRuntimeException {

    public ConfigException(String content) {
        super(content);
    }

    public ConfigException(Integer code, String content) {
        super(code, content);
    }

    public ConfigException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}

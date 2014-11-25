package message.cache.configs;

import message.base.exception.ApplicationRuntimeException;

/**
 * 解析缓存配置文件发生的异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-8-29 下午9:22
 */
public class CacheException extends ApplicationRuntimeException {

    public CacheException(int errorCode) {
        super(errorCode);
    }

    public CacheException(int errorCode, Throwable exception, String message, String... args) {
        super(errorCode, exception, message, args);
    }

    public CacheException(int errorCode, Throwable exception) {
        super(errorCode, exception);
    }

    public CacheException(int errorCode, String message, String... args) {
        super(errorCode, message, args);
    }
}

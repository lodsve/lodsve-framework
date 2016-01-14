package message.cache.configs;

import message.base.exception.ApplicationRuntimeException;

/**
 * 解析缓存配置文件发生的异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-8-29 下午9:22
 */
public class CacheException extends ApplicationRuntimeException {

    public CacheException(Integer code, String content) {
        super(code, content);
    }

    public CacheException(Integer code, String content, String... args) {
        super(code, content, args);
    }

    public CacheException(String content) {
        super(content);
    }
}

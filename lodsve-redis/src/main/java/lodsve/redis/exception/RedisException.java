package lodsve.redis.exception;

import lodsve.core.exception.ApplicationException;

/**
 * redis异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/6/21 下午3:19
 */
public class RedisException extends ApplicationException {
    public RedisException(String content) {
        super(content);
    }

    public RedisException(Integer code, String content) {
        super(code, content);
    }

    public RedisException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}

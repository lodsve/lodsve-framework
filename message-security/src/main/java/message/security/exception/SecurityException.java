package message.security.exception;

import message.base.exception.ApplicationRuntimeException;

/**
 * 用户已存在的异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-5 20:43
 */
public class SecurityException extends ApplicationRuntimeException {
    public SecurityException(String content) {
        super(content);
    }

    public SecurityException(Integer code, String content) {
        super(code, content);
    }

    public SecurityException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}

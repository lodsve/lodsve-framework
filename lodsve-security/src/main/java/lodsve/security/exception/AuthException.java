package lodsve.security.exception;

import lodsve.core.exception.ApplicationException;

/**
 * 认证异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/6/21 下午3:47
 */
public class AuthException extends ApplicationException {
    public AuthException(String content) {
        super(content);
    }

    public AuthException(Integer code, String content) {
        super(code, content);
    }

    public AuthException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}

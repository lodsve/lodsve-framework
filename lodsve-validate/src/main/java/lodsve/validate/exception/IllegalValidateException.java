package lodsve.validate.exception;

import lodsve.core.exception.ApplicationException;

/**
 * 校验不合法异常.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2017/12/12 15:42
 */
public class IllegalValidateException extends ApplicationException {
    public IllegalValidateException(String content) {
        super(content);
    }

    public IllegalValidateException(Integer code, String content) {
        super(code, content);
    }

    public IllegalValidateException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}

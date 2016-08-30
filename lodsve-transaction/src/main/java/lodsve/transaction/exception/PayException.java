package lodsve.transaction.exception;

import lodsve.core.exception.ApplicationException;

/**
 * 支付异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/6/22 下午12:44
 */
public class PayException extends ApplicationException {
    public PayException(String content) {
        super(content);
    }

    public PayException(Integer code, String content) {
        super(code, content);
    }

    public PayException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}

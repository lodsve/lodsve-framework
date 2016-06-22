package lodsve.transaction.exception;

import lodsve.core.exception.ApplicationException;

/**
 * 退款异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/6/22 下午12:45
 */
public class RefundException extends ApplicationException {
    public RefundException(String content) {
        super(content);
    }

    public RefundException(Integer code, String content) {
        super(code, content);
    }

    public RefundException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}

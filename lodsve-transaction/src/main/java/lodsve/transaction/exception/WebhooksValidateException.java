package lodsve.transaction.exception;

import lodsve.core.exception.ApplicationException;

/**
 * 回调校验异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/6/22 下午12:46
 */
public class WebhooksValidateException extends ApplicationException {
    public WebhooksValidateException(String content) {
        super(content);
    }

    public WebhooksValidateException(Integer code, String content) {
        super(code, content);
    }

    public WebhooksValidateException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}

package message.transaction;

import message.base.exception.ApplicationRuntimeException;

/**
 * 无效的ping++回调.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/10/27 上午2:15
 */
public class InvalidWebhooksException extends ApplicationRuntimeException {
    public InvalidWebhooksException(String content) {
        super(content);
    }

    public InvalidWebhooksException(Integer code, String content) {
        super(code, content);
    }

    public InvalidWebhooksException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}

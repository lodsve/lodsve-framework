package lodsve.wechat.exception;

import lodsve.core.exception.ApplicationException;

/**
 * 异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/25 上午10:05
 */
public class WeChatException extends ApplicationException {
    public WeChatException(String content) {
        super(content);
    }

    public WeChatException(Integer code, String content) {
        super(code, content);
    }

    public WeChatException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}

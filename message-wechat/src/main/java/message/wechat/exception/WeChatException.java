package message.wechat.exception;

import message.mvc.exception.ApplicationRuntimeException;

/**
 * 异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/25 上午10:05
 */
public class WeChatException extends ApplicationRuntimeException {
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

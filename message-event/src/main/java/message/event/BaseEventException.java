package message.event;

import message.base.exception.ApplicationRuntimeException;

/**
 * 事件的定义异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 13-4-27 上午5:45
 */
public class BaseEventException extends ApplicationRuntimeException {
    public BaseEventException(String content) {
        super(content);
    }

    public BaseEventException(Integer code, String content) {
        super(code, content);
    }

    public BaseEventException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}

package message.validate.exception;

import message.base.exception.ApplicationRuntimeException;

/**
 * 正则表达式的验证字符为空的异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-11-26 下午9:43
 */
public class EmptyRegexPattern extends ApplicationRuntimeException {
    public EmptyRegexPattern(String content) {
        super(content);
    }

    public EmptyRegexPattern(Integer code, String content) {
        super(code, content);
    }

    public EmptyRegexPattern(Integer code, String content, String... args) {
        super(code, content, args);
    }
}

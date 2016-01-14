package message.validate.exception;

import message.base.exception.ApplicationRuntimeException;

/**
 * 验证没有通过的异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-12-2 上午12:58
 */
public class DataValidateException extends ApplicationRuntimeException {
    public DataValidateException(String content) {
        super(content);
    }

    public DataValidateException(Integer code, String content) {
        super(code, content);
    }

    public DataValidateException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}

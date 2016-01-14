package message.mybatis.exception;

import message.base.exception.ApplicationRuntimeException;

/**
 * mybatis异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/7/14 下午2:20
 */
public class MybatisException extends ApplicationRuntimeException {
    public MybatisException(Integer code, String content) {
        super(code, content);
    }

    public MybatisException(Integer code, String content, String... args) {
        super(code, content, args);
    }

    public MybatisException(String content) {
        super(content);
    }
}

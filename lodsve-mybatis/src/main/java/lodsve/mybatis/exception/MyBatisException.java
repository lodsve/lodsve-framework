package lodsve.mybatis.exception;

import lodsve.core.exception.ApplicationException;

/**
 * mybatis异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 16/8/15 下午4:10
 */
public class MyBatisException extends ApplicationException {
    public MyBatisException(String content) {
        super(content);
    }

    public MyBatisException(Integer code, String content) {
        super(code, content);
    }

    public MyBatisException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}

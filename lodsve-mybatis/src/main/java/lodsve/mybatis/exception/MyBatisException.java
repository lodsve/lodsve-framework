package lodsve.mybatis.exception;

import lodsve.core.exception.ApplicationException;

/**
 * mybatis异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/6/21 下午3:19
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

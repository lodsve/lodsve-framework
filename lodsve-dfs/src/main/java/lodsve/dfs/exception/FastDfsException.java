package lodsve.dfs.exception;

import lodsve.core.exception.ApplicationException;

/**
 * 异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016/10/19 下午8:27
 */
public class FastDfsException extends ApplicationException {
    public FastDfsException(String content) {
        super(content);
    }

    public FastDfsException(Integer code, String content) {
        super(code, content);
    }

    public FastDfsException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}

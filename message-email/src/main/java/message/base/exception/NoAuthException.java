package message.base.exception;

/**
 * 认证信息错误的异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 13-3-25 上午8:46
 */
public class NoAuthException extends ApplicationRuntimeException {

    public NoAuthException(String content) {
        super(content);
    }

    public NoAuthException(Integer code, String content) {
        super(code, content);
    }

    public NoAuthException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}

package message.search;

import message.base.exception.ApplicationRuntimeException;

/**
 * 搜索引擎初始化自定义异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 13-5-5 下午2:29
 */
public class SearchInitException extends ApplicationRuntimeException {

    public SearchInitException(int errorCode) {
        super(errorCode);
    }

    public SearchInitException(int errorCode, Throwable exception, String message, String... args) {
        super(errorCode, exception, message, args);
    }

    public SearchInitException(int errorCode, Throwable exception) {
        super(errorCode, exception);
    }

    public SearchInitException(int errorCode, String message, String... args) {
        super(errorCode, message, args);
    }
}

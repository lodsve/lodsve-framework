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

    public SearchInitException(String content) {
        super(content);
    }

    public SearchInitException(Integer code, String content) {
        super(code, content);
    }

    public SearchInitException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}

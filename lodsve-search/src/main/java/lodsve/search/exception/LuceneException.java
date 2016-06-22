package lodsve.search.exception;

import lodsve.core.exception.ApplicationException;

/**
 * Lucene异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/6/21 下午3:19
 */
public class LuceneException extends ApplicationException {
    public LuceneException(String content) {
        super(content);
    }

    public LuceneException(Integer code, String content) {
        super(code, content);
    }

    public LuceneException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}

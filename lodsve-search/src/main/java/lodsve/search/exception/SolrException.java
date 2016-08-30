package lodsve.search.exception;

import lodsve.core.exception.ApplicationException;

/**
 * solr异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/6/21 下午3:19
 */
public class SolrException extends ApplicationException {
    public SolrException(String content) {
        super(content);
    }

    public SolrException(Integer code, String content) {
        super(code, content);
    }

    public SolrException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}

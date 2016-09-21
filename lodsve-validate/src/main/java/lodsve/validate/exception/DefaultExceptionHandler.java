package lodsve.validate.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 默认的异常处理类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 12-12-2 上午1:11
 */
public class DefaultExceptionHandler implements ExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    public void doHandleException(List<ErrorMessage> messages) throws Exception {
        // TODO 异常处理
    }
}

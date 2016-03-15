package message.amqp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

/**
 * 默认异常处理.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2014-10-7 13:18
 */
@Component
public class DefaultErrorHandler implements ErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(DefaultErrorHandler.class);

    @Override
    public void handleError(Throwable t) {
        logger.error("RabbitMQ happen a error:" + t.getMessage(), t);
    }
}

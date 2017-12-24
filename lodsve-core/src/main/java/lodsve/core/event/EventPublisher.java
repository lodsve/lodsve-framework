package lodsve.core.event;

import lodsve.core.event.module.BaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 事件发布器.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 13-4-27 上午4:57
 */
@Component
public class EventPublisher {
    /**
     * Logger.
     */
    protected static final Logger logger = LoggerFactory.getLogger(EventPublisher.class);

    /**
     * 事件执行者
     */
    @Autowired
    private EventExecutor eventExecutor;

    /**
     * 发布事件
     *
     * @param baseEvent
     */
    public void publish(BaseEvent baseEvent) {
        logger.debug("****************execute module '{}' start!", baseEvent);
        eventExecutor.executeEvent(baseEvent);
        logger.debug("****************execute module '{}' stop!", baseEvent);
    }

    /**
     * 解析中文名
     *
     * @param event 事件
     * @return 中文名
     */
    public String evalName(Class<? extends BaseEvent> event) {
        return eventExecutor.evalName(event);
    }
}

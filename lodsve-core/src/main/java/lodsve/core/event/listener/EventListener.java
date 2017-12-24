package lodsve.core.event.listener;

import lodsve.core.event.module.BaseEvent;

/**
 * 事件监听器.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 13-4-27 上午5:07
 */
public interface EventListener<T extends BaseEvent> {
    /**
     * 处理事件
     *
     * @param baseEvent 事件
     * @throws RuntimeException
     */
    void handleEvent(T baseEvent) throws RuntimeException;
}

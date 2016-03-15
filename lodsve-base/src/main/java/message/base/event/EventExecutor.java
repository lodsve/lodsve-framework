package message.base.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import message.base.event.listener.AbstractEventListener;
import message.base.event.module.BaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 事件执行器.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 13-4-27 上午4:57
 */
@Component
public class EventExecutor {
    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(EventExecutor.class);

    /**
     * 同步事件Map.
     */
    private final Map<Class<? extends BaseEvent>, List<AbstractEventListener>> syncEventListeners = new HashMap<>();

    /**
     * 异步事件Map.
     */
    private final Map<Class<? extends BaseEvent>, List<AbstractEventListener>> asyncEventListeners = new HashMap<>();

    private final Object REGISTER_LOCK_OBJECT = new Object();

    /**
     * 注册监听服务
     *
     * @param eventTypes 事件类型
     * @param listener   监听器
     * @param isSync     是否是同步执行
     */
    public void registerListener(List<Class<? extends BaseEvent>> eventTypes, AbstractEventListener listener, boolean isSync) {
        synchronized (REGISTER_LOCK_OBJECT) {
            if (eventTypes == null || eventTypes.isEmpty()) {
                logger.debug("module types is null!");
                return;
            }

            logger.debug("regist listener '{}' for module type '{}'!", listener, eventTypes);

            Map<Class<? extends BaseEvent>, List<AbstractEventListener>> eventListeners = isSync ? syncEventListeners : asyncEventListeners;
            for (Iterator<Class<? extends BaseEvent>> it = eventTypes.iterator(); it.hasNext(); ) {
                Class<? extends BaseEvent> eventType = it.next();
                if (eventType == null)
                    continue;

                List<AbstractEventListener> listeners = eventListeners.get(eventType);
                if (listeners == null) {
                    listeners = new ArrayList<>();
                }

                listeners.add(listener);
                eventListeners.put(eventType, listeners);
            }
        }
    }

    /**
     * 执行事件
     *
     * @param event
     */
    public void executeEvent(BaseEvent event) throws RuntimeException {
        //1.先执行同步事件
        List<AbstractEventListener> syncListeners = syncEventListeners.get(event.getClass());
        if (syncListeners != null && !syncListeners.isEmpty()) {
            execute(syncListeners, event);
        }

        //2.执行异步事件
        List<AbstractEventListener> asyncListeners = asyncEventListeners.get(event.getClass());
        if (asyncListeners != null && !asyncListeners.isEmpty()) {
            executeAsyncEvent(asyncListeners, event);
        }
    }

    /**
     * 执行异步事件
     *
     * @param asyncListeners 异步事件监听
     * @param event          异步事件
     */
    private void executeAsyncEvent(final List<AbstractEventListener> asyncListeners, final BaseEvent event) throws RuntimeException {
        new Thread() {
            public void run() {
                execute(asyncListeners, event);
            }
        }.start();
    }

    private void execute(List<AbstractEventListener> listeners, BaseEvent event) {
        for (Iterator<AbstractEventListener> it = listeners.iterator(); it.hasNext(); ) {
            AbstractEventListener listener = it.next();

            logger.debug("execute module '{}' use listener '{}'!", event, listener);
            //执行
            try {
                listener.handleEvent(event);
            } catch (RuntimeException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}

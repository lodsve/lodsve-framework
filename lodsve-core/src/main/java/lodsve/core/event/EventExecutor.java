/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lodsve.core.event;

import lodsve.core.event.annotations.AsyncEvent;
import lodsve.core.event.annotations.Events;
import lodsve.core.event.annotations.SyncEvent;
import lodsve.core.event.listener.EventListener;
import lodsve.core.event.module.BaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * 事件执行器.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 13-4-27 上午4:57
 */
public class EventExecutor implements InitializingBean {
    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(EventExecutor.class);

    private final ExecutorService executorService;
    @Autowired(required = false)
    private List<EventListener> eventListeners;

    /**
     * 同步事件Map.
     */
    private final Map<Class<? extends BaseEvent>, List<EventListener>> syncEventListeners = new HashMap<>();

    /**
     * 异步事件Map.
     */
    private final Map<Class<? extends BaseEvent>, List<EventListener>> asyncEventListeners = new HashMap<>();
    /**
     * 事件class类型 --> 中文描述
     */
    private final Map<Class<? extends BaseEvent>, String> operationEvents = new HashMap<>();

    private final Object REGISTER_LOCK_OBJECT = new Object();

    public EventExecutor(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * 注册监听服务
     *
     * @param eventType 事件类型
     * @param listener  监听器
     * @param isSync    是否是同步执行
     */
    private void registerListener(Class<? extends BaseEvent> eventType, EventListener listener, String name, boolean isSync) {
        synchronized (REGISTER_LOCK_OBJECT) {
            if (eventType == null) {
                logger.debug("module types is null!");
                return;
            }

            logger.debug("regist listener '{}' for module type '{}'!", listener, eventType);

            Map<Class<? extends BaseEvent>, List<EventListener>> eventListeners = isSync ? syncEventListeners : asyncEventListeners;

            List<EventListener> listeners = eventListeners.get(eventType);
            if (listeners == null) {
                listeners = new ArrayList<>();
            }

            listeners.add(listener);
            eventListeners.put(eventType, listeners);

            operationEvents.put(eventType, name);
        }
    }

    /**
     * 执行事件
     *
     * @param event 事件
     */
    void executeEvent(BaseEvent event) throws RuntimeException {
        //1.先执行同步事件
        List<EventListener> syncListeners = syncEventListeners.get(event.getClass());
        if (syncListeners != null && !syncListeners.isEmpty()) {
            execute(syncListeners, event);
        }

        //2.执行异步事件
        List<EventListener> asyncListeners = asyncEventListeners.get(event.getClass());
        if (asyncListeners != null && !asyncListeners.isEmpty()) {
            executeAsyncEvent(asyncListeners, event);
        }
    }

    /**
     * 解析中文名
     *
     * @param event 事件
     * @return 中文名
     */
    String evalName(Class<? extends BaseEvent> event) {
        Assert.notNull(event);
        return operationEvents.get(event);
    }

    /**
     * 执行异步事件
     *
     * @param asyncListeners 异步事件监听
     * @param event          异步事件
     */
    private void executeAsyncEvent(final List<EventListener> asyncListeners, final BaseEvent event) throws RuntimeException {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                execute(asyncListeners, event);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void execute(List<EventListener> listeners, BaseEvent event) {
        for (EventListener listener : listeners) {
            logger.debug("execute module '{}' use listener '{}'!", event, listener);
            //执行
            try {
                listener.handleEvent(event);
            } catch (RuntimeException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (eventListeners == null) {
            return;
        }

        for (EventListener listener : eventListeners) {
            Class<? extends EventListener> clazz = listener.getClass();
            List<SyncEvent> syncEvents = new ArrayList<>(16);
            List<AsyncEvent> asyncEvents = new ArrayList<>(16);

            Events events = clazz.getAnnotation(Events.class);
            SyncEvent syncEvent = clazz.getAnnotation(SyncEvent.class);
            AsyncEvent asyncEvent = clazz.getAnnotation(AsyncEvent.class);

            syncEvents.add(syncEvent);
            syncEvents.addAll(Arrays.asList(events.sync()));
            asyncEvents.add(asyncEvent);
            asyncEvents.addAll(Arrays.asList(events.async()));

            for (SyncEvent event : syncEvents) {
                if (event == null) {
                    continue;
                }
                registerListener(event.event(), listener, event.name(), true);
            }

            for (AsyncEvent event : asyncEvents) {
                if (event == null) {
                    continue;
                }
                registerListener(event.event(), listener, event.name(), false);
            }
        }
    }
}

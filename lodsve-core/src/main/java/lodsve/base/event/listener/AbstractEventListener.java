package lodsve.base.event.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lodsve.base.event.EventExecutor;
import lodsve.base.event.module.BaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 事件监听器.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 13-4-27 上午5:07
 */
@Component
public abstract class AbstractEventListener implements InitializingBean {
    /**
     * Logger.
     */
    protected final transient Logger logger = LoggerFactory.getLogger(AbstractEventListener.class);
    /**
     * 事件处理类.
     */
    @Autowired
    private EventExecutor eventExecutor;

    /**
     * 同步事件类型
     */
    protected List<Class<? extends BaseEvent>> syncEventListeners = new ArrayList<>();
    /**
     * 异步事件类型
     */
    protected List<Class<? extends BaseEvent>> asyncEventListeners = new ArrayList<>();

    /**
     * 事件class类型 --> 中文描述
     */
    protected Map<Class<? extends BaseEvent>, String> operationEvents = new HashMap<>();

    protected AbstractEventListener() {
    }

    public abstract void handleEvent(BaseEvent baseEvent) throws RuntimeException;

    public void afterPropertiesSet() throws Exception {
        //注册事件监听
        if (syncEventListeners != null) {
            eventExecutor.registerListener(syncEventListeners, this, true);
        }

        if (asyncEventListeners != null) {
            eventExecutor.registerListener(asyncEventListeners, this, false);
        }
    }

    /**
     * 设置同步异步事件
     *
     * @param operationEvents 事件的map[Class, 中文名]
     * @param isSync          是否是同步事件
     */
    public void setOperationEvents(Map<Class<? extends BaseEvent>, String> operationEvents, boolean isSync) {
        if (operationEvents == null || operationEvents.isEmpty()) {
            return;
        }

        List<Class<? extends BaseEvent>> operations = new ArrayList<>();
        for (Class<? extends BaseEvent> moduleClass : operationEvents.keySet()) {
            operations.add(moduleClass);
        }

        if (isSync) {
            // 同步
            this.syncEventListeners.addAll(operations);
        } else {
            // 异步
            this.asyncEventListeners.addAll(operations);
        }

        this.operationEvents.putAll(operationEvents);
    }
}

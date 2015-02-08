package message.event.listener;

import message.event.BaseEventException;
import message.event.EventExecutor;
import message.event.module.BaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
     * 是否是同步执行该事件.
     */
    protected boolean sync = false;
    /**
     * 事件处理类.
     */
    @Autowired
    private EventExecutor eventExecutor;

    /**
     * 事件类型
     */
    protected Class<?> eventType;
    protected List<Class<?>> eventTypes;

    protected Map<Class, String> operationEvents;

    protected AbstractEventListener() {
    }

    /**
     * 构造器
     *
     * @param eventType
     */
    public AbstractEventListener(Class<?> eventType) {
        this.eventType = eventType;
    }

    /**
     * 构造器
     *
     * @param eventTypes
     */
    public AbstractEventListener(List<Class<?>> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public abstract void handleEvent(BaseEvent baseEvent) throws BaseEventException;

    public void afterPropertiesSet() throws Exception {
        //注册事件监听
        if(eventType != null){
            eventExecutor.registerListener(eventType, this, sync);
        }

        if(eventTypes != null && !eventTypes.isEmpty()){
            eventExecutor.registerListener(eventTypes, this, sync);
        }
    }

    public void setOperationEvents(Map<Class, String> operationEvents) {
        List<Class<?>> operations = new ArrayList<Class<?>>();
        if(operationEvents != null && !operationEvents.isEmpty()){
            for(Class<?> moduleClass : operationEvents.keySet()){
                operations.add(moduleClass);
            }
        }

        this.setEventTypes(operations);
        this.operationEvents = operationEvents;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public void setEventType(Class<?> eventType) {
        this.eventType = eventType;
    }

    public void setEventTypes(List<Class<?>> eventTypes) {
        this.eventTypes = eventTypes;
    }
}

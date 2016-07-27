package lodsve.core.event.module;

import java.io.Serializable;
import java.util.Date;
import java.util.EventObject;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 事件的基类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 13-4-27 上午5:00
 */
public class BaseEvent extends EventObject implements Serializable {
    /**
     * Logger.
     */
    protected static final Logger logger = LoggerFactory.getLogger(BaseEvent.class);

    /**
     * 常量，在map中key值
     */
    public static final String CLIENT_IP = "client_ip";
    public static final String DESCRIPTION = "description";

    /**
     * 触发事件的操作者，可选.
     */
    protected Long operatorId;

    /**
     * 事件的直接关系人，可选.
     */
    protected Long ownerId;

    /**
     * 资源类型
     */
    protected Integer resourceType;

    /**
     * 资源id
     */
    protected Long resourceId;

    /**
     * 事件的值栈
     */
    protected Map params;

    /**
     * 事件发布时间.
     */
    protected Date publishTime = new Date();

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public BaseEvent(Object source) {
        super(source);
    }

    public BaseEvent(Object source, Long operatorId, Long ownerId, Integer resourceType, Long resourceId) {
        super(source);
        this.operatorId = operatorId;
        this.ownerId = ownerId;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public BaseEvent(Object source, Long operatorId, Long ownerId, Integer resourceType, Long resourceId, Map params) {
        super(source);
        this.operatorId = operatorId;
        this.ownerId = ownerId;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.params = params;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Map getParams() {
        return params;
    }

    public void setParams(Map params) {
        this.params = params;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }
}

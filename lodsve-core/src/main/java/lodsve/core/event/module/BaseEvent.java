package lodsve.core.event.module;

import java.io.Serializable;
import java.util.Date;
import java.util.EventObject;

/**
 * 事件的基类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 13-4-27 上午5:00
 */
public class BaseEvent extends EventObject implements Serializable {
    private Date eventTime;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public BaseEvent(Object source, Date eventTime) {
        super(source);
        this.eventTime = eventTime;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }
}

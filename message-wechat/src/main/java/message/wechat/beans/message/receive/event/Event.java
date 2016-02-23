package message.wechat.beans.message.receive.event;

import javax.xml.bind.annotation.XmlElement;
import message.wechat.beans.message.EventType;
import message.wechat.beans.message.MsgType;
import message.wechat.beans.message.receive.Receive;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午11:03
 */
public class Event extends Receive {
    @XmlElement(name = "Event")
    private EventType event;
    @XmlElement(name = "EventKey")
    private String eventKey;

    public Event() {
        setMsgType(MsgType.event);
    }

    public EventType getEvent() {
        return event;
    }

    public void setEvent(EventType event) {
        this.event = event;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }
}

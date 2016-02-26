package message.wechat.beans.message.receive.event;

import com.alibaba.fastjson.annotation.JSONField;
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
    @JSONField(name = "Event")
    public EventType event;
    @XmlElement(name = "EventKey")
    @JSONField(name = "EventKey")
    public String eventKey;

    public Event() {
        msgType = MsgType.event;
    }
}

package message.wechat.beans.message.receive;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import message.wechat.beans.message.MsgType;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午10:40
 */
public class Receive implements Serializable {
    @XmlElement(name = "ToUserName")
    @JSONField(name = "ToUserName")
    public String toUserName;
    @XmlElement(name = "FromUserName")
    @JSONField(name = "FromUserName")
    public String fromUserName;
    @XmlElement(name = "MsgType")
    @JSONField(name = "MsgType")
    public MsgType msgType;
    @XmlElement(name = "CreateTime")
    @JSONField(name = "CreateTime")
    public long createTime;
    @XmlElement(name = "MsgId")
    @JSONField(name = "MsgId")
    public long msgId;
}

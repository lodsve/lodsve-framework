package message.wechat.beans.message.receive.msg;

import com.alibaba.fastjson.annotation.JSONField;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import message.wechat.beans.message.receive.Receive;

/**
 * 文本消息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午10:45
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class TextMsg extends Receive {
    @XmlElement(name = "Content")
    @JSONField(name = "Content")
    public String content;
}

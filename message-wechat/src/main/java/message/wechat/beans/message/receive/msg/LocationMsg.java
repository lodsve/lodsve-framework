package message.wechat.beans.message.receive.msg;

import com.alibaba.fastjson.annotation.JSONField;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import message.wechat.beans.message.receive.Receive;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午10:48
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class LocationMsg extends Receive {
    @XmlElement(name = "Location_X")
    @JSONField(name = "Location_X")
    public float locationX;
    @XmlElement(name = "Location_Y")
    @JSONField(name = "Location_Y")
    public float locationY;
    @XmlElement(name = "Scale")
    @JSONField(name = "Scale")
    public float scale;
    @XmlElement(name = "Label")
    @JSONField(name = "Label")
    public float label;
}

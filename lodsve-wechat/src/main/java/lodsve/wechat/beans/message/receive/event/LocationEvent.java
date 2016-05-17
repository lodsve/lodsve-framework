package lodsve.wechat.beans.message.receive.event;

import com.alibaba.fastjson.annotation.JSONField;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 接收上报地理位置事件.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午11:15
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class LocationEvent extends Event {
    @XmlElement(name = "Latitude")
    @JSONField(name = "Latitude")
    public float latitude;
    @XmlElement(name = "Longitude")
    @JSONField(name = "Longitude")
    public float longitude;
    @XmlElement(name = "Precision")
    @JSONField(name = "Precision")
    public float precision;
}

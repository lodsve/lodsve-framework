package lodsve.wechat.beans.message.receive.msg;

import com.alibaba.fastjson.annotation.JSONField;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lodsve.wechat.beans.message.receive.Receive;

/**
 * 链接消息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午10:48
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class LinkMsg extends Receive {
    @XmlElement(name = "Url")
    @JSONField(name = "Url")
    private String url;
    @XmlElement(name = "Title")
    @JSONField(name = "Title")
    private String title;
    @XmlElement(name = "Description")
    @JSONField(name = "Description")
    private String description;
}

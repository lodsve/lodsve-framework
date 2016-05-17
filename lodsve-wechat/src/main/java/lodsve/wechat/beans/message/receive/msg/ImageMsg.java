package lodsve.wechat.beans.message.receive.msg;

import com.alibaba.fastjson.annotation.JSONField;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lodsve.wechat.beans.message.receive.Receive;

/**
 * 图片消息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午10:47
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ImageMsg extends Receive {
    @XmlElement(name = "PicUrl")
    @JSONField(name = "PicUrl")
    public String picUrl;
    @XmlElement(name = "MediaId")
    @JSONField(name = "MediaId")
    public String mediaId;
}

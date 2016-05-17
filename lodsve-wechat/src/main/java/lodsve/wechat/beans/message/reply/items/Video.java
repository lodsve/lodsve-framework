package lodsve.wechat.beans.message.reply.items;

import com.alibaba.fastjson.annotation.JSONField;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 视频消息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午11:37
 */
@XmlRootElement(name = "Video")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Video extends Media {
    @XmlElement(name = "Title")
    @JSONField(name = "Title")
    public String title;
    @XmlElement(name = "Description")
    @JSONField(name = "Description")
    public String description;
}

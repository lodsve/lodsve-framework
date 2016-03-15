package message.wechat.beans.message.reply;

import com.alibaba.fastjson.annotation.JSONField;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import message.wechat.beans.message.ReplyType;
import message.wechat.beans.message.reply.items.Image;

/**
 * 回复图片消息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午11:31
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ImageReply extends Reply {
    public ImageReply() {
        msgType = ReplyType.image;
    }

    @XmlElement(name = "Image")
    @JSONField(name = "Image")
    public Image image;
}

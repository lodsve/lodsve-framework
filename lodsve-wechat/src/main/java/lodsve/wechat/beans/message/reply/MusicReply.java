package lodsve.wechat.beans.message.reply;

import com.alibaba.fastjson.annotation.JSONField;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lodsve.wechat.beans.message.ReplyType;
import lodsve.wechat.beans.message.reply.items.Music;

/**
 * 回复音乐消息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午11:38
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class MusicReply extends Reply {
    public MusicReply() {
        msgType = ReplyType.music;
    }

    @XmlElement(name = "Music")
    @JSONField(name = "Music")
    public Music music;
}

package message.wechat.beans.message.reply;

import com.alibaba.fastjson.annotation.JSONField;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import message.wechat.beans.message.ReplyType;
import message.wechat.beans.message.reply.items.Voice;

/**
 * 回复语音消息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午11:34
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class VoiceReply extends Reply {
    public VoiceReply() {
        msgType = ReplyType.voice;
    }

    @XmlElement(name = "Voice")
    @JSONField(name = "Voice")
    public Voice voice;
}

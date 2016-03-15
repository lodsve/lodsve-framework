package message.wechat.beans.message.reply.items;

import com.alibaba.fastjson.annotation.JSONField;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * music消息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午11:39
 */
@XmlRootElement(name = "Music")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Music extends Media {
    @XmlElement(name = "Title")
    @JSONField(name = "Title")
    public String title;
    @XmlElement(name = "Description")
    @JSONField(name = "Description")
    public String description;
    @XmlElement(name = "MusicUrl")
    @JSONField(name = "MusicUrl")
    public String musicUrl;
    @XmlElement(name = "HQMusicUrl")
    @JSONField(name = "HQMusicUrl")
    public String hqMusicUrl;
    @XmlElement(name = "ThumbMediaId")
    @JSONField(name = "ThumbMediaId")
    public String thumbMediaId;
}

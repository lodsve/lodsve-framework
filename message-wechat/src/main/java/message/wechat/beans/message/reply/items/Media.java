package message.wechat.beans.message.reply.items;

import com.alibaba.fastjson.annotation.JSONField;
import javax.xml.bind.annotation.XmlElement;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午11:32
 */
public class Media {
    @XmlElement(name = "MediaId")
    @JSONField(name = "MediaId")
    public String mediaId;
}

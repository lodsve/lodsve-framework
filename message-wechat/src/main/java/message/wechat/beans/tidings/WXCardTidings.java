package message.wechat.beans.tidings;

import com.fasterxml.jackson.annotation.JsonProperty;
import message.wechat.beans.tidings.items.WXCard;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 下午12:18
 */
public class WXCardTidings extends Tidings {
    public WXCardTidings() {
        setTidingsType(TidingsType.wxcard);
    }

    @JsonProperty("wxcard")
    private WXCard wxCard;

    public WXCard getWxCard() {
        return wxCard;
    }

    public void setWxCard(WXCard wxCard) {
        this.wxCard = wxCard;
    }
}

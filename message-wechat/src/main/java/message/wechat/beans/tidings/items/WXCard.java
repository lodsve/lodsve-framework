package message.wechat.beans.tidings.items;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 卡券.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 下午12:24
 */
public class WXCard {
    @JsonProperty("card_id")
    private String cardId;
    @JsonProperty("card_ext")
    private String cardExt;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardExt() {
        return cardExt;
    }

    public void setCardExt(String cardExt) {
        this.cardExt = cardExt;
    }
}

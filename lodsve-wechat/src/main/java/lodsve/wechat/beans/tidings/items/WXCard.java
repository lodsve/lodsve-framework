package lodsve.wechat.beans.tidings.items;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 卡券.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 下午12:24
 */
@ApiModel(description = "卡券")
public class WXCard {
    @ApiModelProperty(value = "卡券ID", required = true)
    @JsonProperty("card_id")
    private String cardId;
    @ApiModelProperty(value = "卡券自定义消息", required = true)
    @JsonProperty("card_ext")
    private String cardExt;

    @ApiModelProperty(value = "卡券ID", required = true)
    public String getCardId() {
        return cardId;
    }

    @ApiModelProperty(value = "卡券ID", required = true)
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    @ApiModelProperty(value = "卡券自定义消息", required = true)
    public String getCardExt() {
        return cardExt;
    }

    @ApiModelProperty(value = "卡券自定义消息", required = true)
    public void setCardExt(String cardExt) {
        this.cardExt = cardExt;
    }
}

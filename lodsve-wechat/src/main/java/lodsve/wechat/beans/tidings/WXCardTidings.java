package lodsve.wechat.beans.tidings;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lodsve.wechat.beans.tidings.items.WXCard;

/**
 * 卡券客服消息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 下午12:18
 */
@ApiModel(description = "卡券客服消息")
public class WXCardTidings extends Tidings {
    public WXCardTidings() {
        setTidingsType(TidingsType.wxcard);
    }

    @ApiModelProperty(value = "卡券", required = true)
    @JsonProperty("wxcard")
    private WXCard wxCard;

    @ApiModelProperty(value = "卡券", required = true)
    public WXCard getWxCard() {
        return wxCard;
    }

    @ApiModelProperty(value = "卡券", required = true)
    public void setWxCard(WXCard wxCard) {
        this.wxCard = wxCard;
    }
}

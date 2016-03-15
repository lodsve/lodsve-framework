package message.wechat.beans.tidings;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import message.wechat.beans.tidings.items.Media;

/**
 * 图文客服消息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 下午12:17
 */
@ApiModel(description = "图文客服消息")
public class MpNewsTidings extends Tidings {
    public MpNewsTidings() {
        setTidingsType(TidingsType.mpnews);
    }

    @ApiModelProperty(value = "图文", required = true)
    private Media mpnews;

    @ApiModelProperty(value = "图文", required = true)
    public Media getMpnews() {
        return mpnews;
    }

    @ApiModelProperty(value = "图文", required = true)
    public void setMpnews(Media mpnews) {
        this.mpnews = mpnews;
    }
}

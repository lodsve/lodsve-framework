package message.wechat.beans.tidings;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import message.wechat.beans.tidings.items.Media;

/**
 * 语音客服消息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 上午11:59
 */
@ApiModel(description = "语音客服消息")
public class VoiceTidings extends Tidings {
    public VoiceTidings() {
        setTidingsType(TidingsType.voice);
    }

    @ApiModelProperty(value = "语音", required = true)
    private Media voice;

    @ApiModelProperty(value = "语音", required = true)
    public Media getVoice() {
        return voice;
    }

    @ApiModelProperty(value = "语音", required = true)
    public void setVoice(Media voice) {
        this.voice = voice;
    }
}

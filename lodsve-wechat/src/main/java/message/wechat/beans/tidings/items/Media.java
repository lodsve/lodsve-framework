package message.wechat.beans.tidings.items;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 媒体消息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 下午12:21
 */
@ApiModel(description = "媒体消息")
public class Media {
    @ApiModelProperty(value = "媒体消息ID", required = true)
    @JsonProperty("media_id")
    private String mediaId;

    @ApiModelProperty(value = "媒体消息ID", required = true)
    public String getMediaId() {
        return mediaId;
    }

    @ApiModelProperty(value = "媒体消息ID", required = true)
    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}

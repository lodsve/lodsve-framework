package message.wechat.beans.tidings.items;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 媒体消息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 下午12:21
 */
public class Media {
    @JsonProperty("media_id")
    private String mediaId;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}

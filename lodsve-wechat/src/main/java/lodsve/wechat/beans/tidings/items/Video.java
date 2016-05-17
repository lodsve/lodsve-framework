package lodsve.wechat.beans.tidings.items;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 视频.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 下午12:24
 */
@ApiModel(description = "视频")
public class Video extends Media {
    @ApiModelProperty(value = "缩略图的媒体ID", required = true)
    @JsonProperty("thumb_media_id")
    private String thumbMediaId;
    @ApiModelProperty(value = "标题", required = true)
    private String title;
    @ApiModelProperty(value = "描述", required = true)
    private String description;

    @ApiModelProperty(value = "缩略图的媒体ID", required = true)
    public String getThumbMediaId() {
        return thumbMediaId;
    }

    @ApiModelProperty(value = "缩略图的媒体ID", required = true)
    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }

    @ApiModelProperty(value = "标题", required = true)
    public String getTitle() {
        return title;
    }

    @ApiModelProperty(value = "标题", required = true)
    public void setTitle(String title) {
        this.title = title;
    }

    @ApiModelProperty(value = "描述", required = true)
    public String getDescription() {
        return description;
    }

    @ApiModelProperty(value = "描述", required = true)
    public void setDescription(String description) {
        this.description = description;
    }
}

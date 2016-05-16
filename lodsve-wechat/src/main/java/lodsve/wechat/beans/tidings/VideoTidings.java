package lodsve.wechat.beans.tidings;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lodsve.wechat.beans.tidings.items.Video;

/**
 * 视频客服消息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 下午12:01
 */
@ApiModel(description = "视频客服消息")
public class VideoTidings extends Tidings {
    public VideoTidings() {
        setTidingsType(TidingsType.video);
    }

    @ApiModelProperty(value = "视频", required = true)
    private Video video;

    @ApiModelProperty(value = "视频", required = true)
    public Video getVideo() {
        return video;
    }

    @ApiModelProperty(value = "视频", required = true)
    public void setVideo(Video video) {
        this.video = video;
    }
}

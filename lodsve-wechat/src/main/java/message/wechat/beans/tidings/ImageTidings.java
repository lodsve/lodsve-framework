package message.wechat.beans.tidings;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import message.wechat.beans.tidings.items.Media;

/**
 * 图片客服消息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 上午11:57
 */
@ApiModel(description = "图片客服消息")
public class ImageTidings extends Tidings {
    public ImageTidings() {
        setTidingsType(TidingsType.image);
    }

    @ApiModelProperty(value = "图片", required = true)
    private Media image;

    @ApiModelProperty(value = "图片", required = true)
    public Media getImage() {
        return image;
    }

    @ApiModelProperty(value = "图片", required = true)
    public void setImage(Media image) {
        this.image = image;
    }
}

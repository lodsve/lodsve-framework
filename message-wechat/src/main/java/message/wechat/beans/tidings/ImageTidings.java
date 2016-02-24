package message.wechat.beans.tidings;

import message.wechat.beans.tidings.items.Media;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 上午11:57
 */
public class ImageTidings extends Tidings {
    public ImageTidings() {
        setTidingsType(TidingsType.image);
    }

    private Media image;

    public Media getImage() {
        return image;
    }

    public void setImage(Media image) {
        this.image = image;
    }
}

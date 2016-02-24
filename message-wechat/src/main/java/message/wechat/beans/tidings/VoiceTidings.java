package message.wechat.beans.tidings;

import message.wechat.beans.tidings.items.Media;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 上午11:59
 */
public class VoiceTidings extends Tidings {
    public VoiceTidings() {
        setTidingsType(TidingsType.voice);
    }

    private Media image;

    public Media getImage() {
        return image;
    }

    public void setImage(Media image) {
        this.image = image;
    }
}

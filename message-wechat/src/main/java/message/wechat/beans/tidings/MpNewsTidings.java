package message.wechat.beans.tidings;

import message.wechat.beans.tidings.items.Media;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 下午12:17
 */
public class MpNewsTidings extends Tidings {
    public MpNewsTidings() {
        setTidingsType(TidingsType.mpnews);
    }

    private Media mpnews;

    public Media getMpnews() {
        return mpnews;
    }

    public void setMpnews(Media mpnews) {
        this.mpnews = mpnews;
    }
}

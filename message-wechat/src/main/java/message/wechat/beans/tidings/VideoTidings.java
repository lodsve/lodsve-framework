package message.wechat.beans.tidings;

import message.wechat.beans.tidings.items.Video;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 下午12:01
 */
public class VideoTidings extends Tidings {
    public VideoTidings() {
        setTidingsType(TidingsType.video);
    }

    private Video video;

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }
}

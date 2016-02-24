package message.wechat.beans.tidings;

import message.wechat.beans.tidings.items.Music;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 下午12:03
 */
public class MusicTidings extends Tidings {
    public MusicTidings() {
        setTidingsType(TidingsType.music);
    }

    private Music music;

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }
}

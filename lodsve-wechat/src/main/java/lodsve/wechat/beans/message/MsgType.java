package lodsve.wechat.beans.message;

/**
 * 接收消息类型.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午10:43
 */
public enum MsgType {
    text, image, voice, video, shortvideo, location, link, event;

    public static MsgType eval(String name) {
        return MsgType.valueOf(name);
    }
}

package message.wechat.beans.message;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午10:43
 */
public enum MsgType {
    text, image, voice, video, shortvideo, location, link, event;

    public static MsgType eval(String name) {
        if ("text".equals(name)) {
            return text;
        } else if ("image".equals(name)) {
            return image;
        } else if ("voice".equals(name)) {
            return voice;
        } else if ("video".equals(name)) {
            return video;
        } else if ("shortvideo".equals(name)) {
            return shortvideo;
        } else if ("location".equals(name)) {
            return location;
        } else if ("event".equals(name)) {
            return event;
        } else {
            return link;
        }
    }
}

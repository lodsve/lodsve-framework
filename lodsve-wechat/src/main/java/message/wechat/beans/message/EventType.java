package message.wechat.beans.message;

/**
 * 接收事件类型.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午11:02
 */
public enum EventType {
    subscribe, unsubscribe, SCAN, LOCATION, CLICK, VIEW;

    public static EventType eval(String name) {
        if ("subscribe".equals(name)) {
            return subscribe;
        } else if ("unsubscribe".equals(name)) {
            return unsubscribe;
        } else if ("SCAN".equals(name)) {
            return SCAN;
        } else if ("LOCATION".equals(name)) {
            return LOCATION;
        } else if ("CLICK".equals(name)) {
            return CLICK;
        } else {
            return VIEW;
        }
    }
}

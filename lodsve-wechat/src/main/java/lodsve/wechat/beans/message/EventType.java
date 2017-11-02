package lodsve.wechat.beans.message;

/**
 * 接收事件类型.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午11:02
 */
public enum EventType {
    subscribe, unsubscribe, SCAN, LOCATION, CLICK, VIEW;

    public static EventType eval(String name) {
        return EventType.valueOf(name);
    }
}

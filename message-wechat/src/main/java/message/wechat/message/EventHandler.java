package message.wechat.message;

import message.wechat.beans.message.EventType;
import message.wechat.beans.message.receive.event.Event;
import message.wechat.beans.message.reply.Reply;

/**
 * 对接受事件处理器.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午11:25
 */
public interface EventHandler<T extends Event> {
    /**
     * 是否支持此事件
     *
     * @param eventType 接受得到的事件
     * @return true/false
     */
    boolean support(EventType eventType);

    /**
     * 处理事件并返回
     *
     * @param event 事件
     * @return 返回消息
     */
    Reply handle(T event);

    /**
     * 获得事件类型
     *
     * @return 事件类型
     */
    Class<T> getType();
}

package message.wechat.beans.message.handler;

import message.wechat.beans.message.EventType;
import message.wechat.beans.message.receive.event.Event;
import message.wechat.beans.message.reply.Reply;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午11:25
 */
public interface EventHandler<T extends Event> {
    boolean support(EventType eventType);

    Reply handle(T event);
}

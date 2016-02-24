package message.wechat.message;

import com.alibaba.fastjson.util.TypeUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import message.base.utils.GenericUtils;
import message.wechat.beans.message.EventType;
import message.wechat.beans.message.MsgType;
import message.wechat.beans.message.receive.Receive;
import message.wechat.beans.message.receive.event.Event;
import message.wechat.beans.message.reply.Reply;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午11:24
 */
@Component
public class ReceiveHandler implements ApplicationContextAware {
    private static final String EVENT = "Event";
    private static final String MSG_TYPE = "MsgType";

    private List<MsgHandler> msgHandlers = new ArrayList<>();
    private List<EventHandler> eventHandlers = new ArrayList<>();

    public Reply handle(Map<String, String> params) {
        Reply reply = null;
        if (params.containsKey(EVENT)) {
            //handle event
            EventType eventType = EventType.eval(params.get(EVENT));
            for (EventHandler eventHandler : eventHandlers) {
                if (eventHandler.support(eventType)) {
                    reply = eventHandler.handle((Event) TypeUtils.castToJavaBean(params, getType(eventHandler.getClass())));
                    if (reply != null) {
                        break;
                    }
                }
            }
        } else {
            //handle message
            MsgType msgType = MsgType.eval(params.get(MSG_TYPE));
            for (MsgHandler msgHandler : msgHandlers) {
                if (msgHandler.support(msgType)) {
                    reply = msgHandler.handle((Receive) TypeUtils.castToJavaBean(params, getType(msgHandler.getClass())));
                    if (reply != null) {
                        break;
                    }
                }
            }
        }
        return reply;
    }

    private Class getType(Class clazz) {
        return GenericUtils.getGenericParameter(clazz, 0);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        msgHandlers.addAll(applicationContext.getBeansOfType(MsgHandler.class).values());
        eventHandlers.addAll(applicationContext.getBeansOfType(EventHandler.class).values());
    }
}

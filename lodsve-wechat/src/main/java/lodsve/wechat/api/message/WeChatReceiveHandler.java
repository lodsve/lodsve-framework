/*
 * Copyright (C) 2018  Sun.Hao
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.wechat.api.message;

import com.alibaba.fastjson.util.TypeUtils;
import lodsve.wechat.beans.message.EventType;
import lodsve.wechat.beans.message.MsgType;
import lodsve.wechat.beans.message.receive.Receive;
import lodsve.wechat.beans.message.receive.event.Event;
import lodsve.wechat.beans.message.reply.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 处理微信的消息/事件.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/23 下午11:24
 */
@Component
public class WeChatReceiveHandler {
    private static final String EVENT = "Event";
    private static final String MSG_TYPE = "MsgType";

    @Autowired
    private List<MsgHandler> msgHandlers;
    @Autowired
    private List<EventHandler> eventHandlers;

    @SuppressWarnings("unchecked")
    public Reply handle(Map<String, String> params) {
        Reply reply = null;
        if (params.containsKey(EVENT)) {
            //handle event
            EventType eventType = EventType.eval(params.get(EVENT));
            for (EventHandler eventHandler : eventHandlers) {
                if (eventHandler.support(eventType)) {
                    reply = eventHandler.handle((Event) TypeUtils.castToJavaBean(params, eventHandler.getType()));
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
                    reply = msgHandler.handle((Receive) TypeUtils.castToJavaBean(params, msgHandler.getType()));
                    if (reply != null) {
                        break;
                    }
                }
            }
        }
        return reply;
    }
}

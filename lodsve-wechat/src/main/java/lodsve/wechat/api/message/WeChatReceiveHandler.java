/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

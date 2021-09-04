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

import lodsve.wechat.beans.message.EventType;
import lodsve.wechat.beans.message.receive.event.Event;
import lodsve.wechat.beans.message.reply.Reply;

/**
 * 对接受事件处理器.
 *
 * @author sunhao(sunhao.java @ gmail.com)
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

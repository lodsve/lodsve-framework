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

import lodsve.wechat.beans.message.MsgType;
import lodsve.wechat.beans.message.receive.Receive;
import lodsve.wechat.beans.message.reply.Reply;

/**
 * 对接受消息处理器.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/23 下午11:25
 */
public interface MsgHandler<T extends Receive> {
    /**
     * 是否支持此消息
     *
     * @param msgType 接受得到的消息
     * @return true/false
     */
    boolean support(MsgType msgType);

    /**
     * 处理消息并返回
     *
     * @param msg 消息
     * @return 返回消息
     */
    Reply handle(T msg);

    /**
     * 获得消息类型
     *
     * @return 消息类型
     */
    Class<T> getType();
}

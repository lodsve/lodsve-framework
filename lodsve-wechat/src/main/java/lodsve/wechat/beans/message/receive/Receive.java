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
package lodsve.wechat.beans.message.receive;

import com.alibaba.fastjson.annotation.JSONField;
import lodsve.wechat.beans.message.MsgType;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * 接收微信消息/事件.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/23 下午10:40
 */
public class Receive implements Serializable {
    @XmlElement(name = "ToUserName")
    @JSONField(name = "ToUserName")
    public String toUserName;
    @XmlElement(name = "FromUserName")
    @JSONField(name = "FromUserName")
    public String fromUserName;
    @XmlElement(name = "MsgType")
    @JSONField(name = "MsgType")
    public MsgType msgType;
    @XmlElement(name = "CreateTime")
    @JSONField(name = "CreateTime")
    public long createTime;
    @XmlElement(name = "MsgId")
    @JSONField(name = "MsgId")
    public long msgId;
}

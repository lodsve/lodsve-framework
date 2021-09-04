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
package lodsve.wechat.beans.message.reply;

import com.alibaba.fastjson.annotation.JSONField;
import lodsve.wechat.beans.message.ReplyType;

import javax.xml.bind.annotation.XmlElement;

/**
 * 回复微信消息.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/23 下午11:27
 */
public class Reply {
    @XmlElement(name = "ToUserName")
    @JSONField(name = "ToUserName")
    public String toUserName;
    @XmlElement(name = "FromUserName")
    @JSONField(name = "FromUserName")
    public String fromUserName;
    @XmlElement(name = "MsgType")
    @JSONField(name = "MsgType")
    public ReplyType msgType;
    @XmlElement(name = "CreateTime")
    @JSONField(name = "CreateTime")
    public long createTime;
}

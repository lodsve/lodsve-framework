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
package lodsve.wechat.beans.message.receive.msg;

import com.alibaba.fastjson.annotation.JSONField;
import lodsve.wechat.beans.message.receive.Receive;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 地理位置消息.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/23 下午10:48
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class LocationMsg extends Receive {
    @XmlElement(name = "Location_X")
    @JSONField(name = "Location_X")
    public float locationX;
    @XmlElement(name = "Location_Y")
    @JSONField(name = "Location_Y")
    public float locationY;
    @XmlElement(name = "Scale")
    @JSONField(name = "Scale")
    public float scale;
    @XmlElement(name = "Label")
    @JSONField(name = "Label")
    public String label;
}

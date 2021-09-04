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
package lodsve.wechat.beans.message.reply.items;

import com.alibaba.fastjson.annotation.JSONField;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * music消息.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/23 下午11:39
 */
@XmlRootElement(name = "Music")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Music extends Media {
    @XmlElement(name = "Title")
    @JSONField(name = "Title")
    public String title;
    @XmlElement(name = "Description")
    @JSONField(name = "Description")
    public String description;
    @XmlElement(name = "MusicUrl")
    @JSONField(name = "MusicUrl")
    public String musicUrl;
    @XmlElement(name = "HQMusicUrl")
    @JSONField(name = "HQMusicUrl")
    public String hqMusicUrl;
    @XmlElement(name = "ThumbMediaId")
    @JSONField(name = "ThumbMediaId")
    public String thumbMediaId;
}

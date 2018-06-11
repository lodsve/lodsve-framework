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

package lodsve.wechat.beans.message.receive;

import com.alibaba.fastjson.annotation.JSONField;
import lodsve.wechat.beans.message.MsgType;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * 接收微信消息/事件.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/2/23 下午10:40
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

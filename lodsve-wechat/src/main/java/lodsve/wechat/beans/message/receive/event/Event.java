/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lodsve.wechat.beans.message.receive.event;

import com.alibaba.fastjson.annotation.JSONField;
import lodsve.wechat.beans.message.EventType;
import lodsve.wechat.beans.message.MsgType;
import lodsve.wechat.beans.message.receive.Receive;

import javax.xml.bind.annotation.XmlElement;

/**
 * 微信事件.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/23 下午11:03
 */
public class Event extends Receive {
    @XmlElement(name = "Event")
    @JSONField(name = "Event")
    public EventType event;
    @XmlElement(name = "EventKey")
    @JSONField(name = "EventKey")
    public String eventKey;

    public Event() {
        msgType = MsgType.event;
    }
}

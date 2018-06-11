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
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/2/23 下午10:48
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

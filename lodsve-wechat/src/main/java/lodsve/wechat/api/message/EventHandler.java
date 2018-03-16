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

package lodsve.wechat.api.message;

import lodsve.wechat.beans.message.EventType;
import lodsve.wechat.beans.message.receive.event.Event;
import lodsve.wechat.beans.message.reply.Reply;

/**
 * 对接受事件处理器.
 *
 * @author sunhao(sunhao.java@gmail.com)
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

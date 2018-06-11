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

import lodsve.wechat.beans.message.MsgType;
import lodsve.wechat.beans.message.receive.Receive;
import lodsve.wechat.beans.message.reply.Reply;

/**
 * 对接受消息处理器.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/2/23 下午11:25
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

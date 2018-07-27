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

package lodsve.redis.timer;

import java.io.Serializable;

/**
 * redis事件处理.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 15/9/28 下午5:15
 */
public interface RedisEventHandler {
    /**
     * 处理事件
     *
     * @param key 键
     */
    void handler(Serializable key);

    /**
     * 解析主键
     *
     * @param message
     * @return
     */
    Serializable resolveKey(String message);

    /**
     * 获取目标对象
     *
     * @return
     */
    RedisEventType getEventType();
}

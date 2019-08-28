/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
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

import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

/**
 * redis事件.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 15/9/28 下午4:28
 */
public class RedisEvent extends ApplicationEvent {
    private final Serializable key;
    private final RedisEventType type;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public RedisEvent(Object source, Serializable key, RedisEventType type) {
        super(source);
        this.key = key;
        this.type = type;
    }

    public final Serializable getKey() {
        return key;
    }

    public final RedisEventType getType() {
        return type;
    }
}

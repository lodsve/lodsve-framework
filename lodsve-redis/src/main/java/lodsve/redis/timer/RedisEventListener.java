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
package lodsve.redis.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import java.io.Serializable;

/**
 * 事件处理.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 15/9/28 下午4:27
 */
public class RedisEventListener implements ApplicationListener<RedisEvent> {
    private static final Logger logger = LoggerFactory.getLogger(RedisEventListener.class);

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(RedisEvent event) {
        logger.debug("event is '{}'", event);
        Serializable key = event.getKey();
        RedisEventType type = event.getType();
        logger.debug("the [{}] [{}] is expired!", type, key);

        RedisEventHandler handler = RedisEventUtils.getRedisEventHandler(type.getType());
        if (handler == null) {
            return;
        }

        handler.handler(key);
    }
}

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
package lodsve.core.event;

import lodsve.core.event.module.BaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 事件发布器.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 13-4-27 上午4:57
 */
public class EventPublisher {
    /**
     * Logger.
     */
    protected static final Logger logger = LoggerFactory.getLogger(EventPublisher.class);

    /**
     * 事件执行者
     */
    @Autowired
    private EventExecutor eventExecutor;

    /**
     * 发布事件
     *
     * @param baseEvent
     */
    public void publish(BaseEvent baseEvent) {
        logger.debug("****************execute module '{}' start!", baseEvent);
        eventExecutor.executeEvent(baseEvent);
        logger.debug("****************execute module '{}' stop!", baseEvent);
    }

    /**
     * 解析中文名
     *
     * @param event 事件
     * @return 中文名
     */
    public String evalName(Class<? extends BaseEvent> event) {
        return eventExecutor.evalName(event);
    }
}

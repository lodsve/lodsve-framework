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

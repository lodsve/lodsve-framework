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

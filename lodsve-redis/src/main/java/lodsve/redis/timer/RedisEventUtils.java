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

import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据类名解析处理类.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 15/9/29 下午12:53
 */
public class RedisEventUtils {
    private final static Map<String, RedisEventHandler> HANDLERS = new HashMap<>();

    @Autowired
    public RedisEventUtils(List<RedisEventHandler> handlers) {
        for (RedisEventHandler bean : handlers) {
            RedisEventType type = bean.getEventType();

            RedisEventUtils.HANDLERS.put(type.getType(), bean);
        }
    }

    public static RedisEventHandler getRedisEventHandler(String type) {
        return HANDLERS.get(type);
    }
}

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

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
package lodsve.rabbitmq.binding;

import lodsve.core.utils.StringUtils;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;

import java.util.Collections;

/**
 * fanout queue.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-08-02 13:48
 */
public class FanoutQueueBinding extends Binding {
    public FanoutQueueBinding(FanoutExchange exchange, Queue queue) {
        super(queue.getName(), DestinationType.QUEUE, exchange.getName(), StringUtils.EMPTY, Collections.emptyMap());
    }

    public FanoutQueueBinding(String exchange, String queue) {
        super(queue, DestinationType.QUEUE, exchange, StringUtils.EMPTY, Collections.emptyMap());
    }
}

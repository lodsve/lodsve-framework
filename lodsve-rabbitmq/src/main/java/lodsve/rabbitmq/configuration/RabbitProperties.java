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
package lodsve.rabbitmq.configuration;

import lodsve.core.autoproperties.relaxedbind.annotations.ConfigurationProperties;
import lodsve.core.autoproperties.relaxedbind.annotations.Required;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * rabbit mq base properties.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016-01-15 12:00
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "lodsve.rabbit", locations = "${params.root}/framework/rabbit.properties")
public class RabbitProperties {
    @Required
    private String address;
    @Required
    private String username;
    @Required
    private String password;
    /**
     * false: 异常消息直接丢弃
     * true: 异常消息将被重新传递
     */
    private Boolean requeueRejected = true;

    private Map<String, QueueConfig> queues;
}

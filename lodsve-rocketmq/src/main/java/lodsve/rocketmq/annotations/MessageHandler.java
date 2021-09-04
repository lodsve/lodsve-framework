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
package lodsve.rocketmq.annotations;

import lodsve.rocketmq.configuration.RocketMQProperties;

import java.lang.annotation.*;

/**
 * 标注这个类中含有注解{@link MessageListener}的方法都是消息的消费者.一个handle只能订阅一个topic
 *
 * @author 孙昊(Hulk)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageHandler {

    /**
     * 消费者所属组，默认是在配置文件中配置的，详见{@link RocketMQProperties.Consumer#getGroup()}
     *
     * @return 消费者所属组
     */
    String group() default "${lodsve.rocketmq.consumer.group}";

    /**
     * 这个消息处理方法需要订阅的topic
     *
     * @return 需要订阅的topic
     */
    String topic();
}

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
package lodsve.rocketmq.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Method;

/**
 * 存储消费者相关信息的.
 *
 * @author 孙昊(Hulk)
 */
@Setter
@Getter
@ToString
public class ConsumerBean {
    /**
     * tag
     */
    private String tag;
    /**
     * 消费者对象
     */
    private Object target;
    /**
     * 处理方法
     */
    private Method method;
}

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

import java.lang.annotation.*;

/**
 * 自定义的message listener，加在方法上（这些方法需要在一个类中，并且这个类要有{@link MessageHandler}这个注解），用来标注消息处理方法.
 *
 * @author 孙昊(Hulk)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageListener {
    /**
     * 消息的标签（rocketmq的最佳实践：同一个项目只使用一个topic，不同类型的消息使用tag来进行区分）
     *
     * @return 消息的标签
     */
    String tag();
}

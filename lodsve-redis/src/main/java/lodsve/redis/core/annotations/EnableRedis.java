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
package lodsve.redis.core.annotations;

import lodsve.core.configuration.EnableLodsve;
import lodsve.redis.core.config.RedisConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用redis.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/1/23 下午11:19
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableLodsve
@Import({RedisConfiguration.class, RedisImportSelector.class})
public @interface EnableRedis {
    /**
     * 数据源名称,required
     *
     * @return 数据源名称
     */
    String[] name();

    /**
     * 定时器使用的数据源，如果为空，则表示不用定时器
     *
     * @return 定时器使用的数据源
     */
    String timer() default "";
}

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
package lodsve.cache.annotations;

import lodsve.core.configuration.EnableLodsve;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用cache模块.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016/1/19 15:10
 * @see CacheImportSelector
 * @see CacheMode
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableLodsve
@Import({CacheImportSelector.class})
public @interface EnableCache {
    /**
     * 选择使用的缓存类型
     *
     * @return 缓存类型
     * @see CacheMode
     */
    CacheMode cache() default CacheMode.REDIS;
}

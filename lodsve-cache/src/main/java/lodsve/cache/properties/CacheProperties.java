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
package lodsve.cache.properties;

import lodsve.core.autoproperties.relaxedbind.annotations.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * cache配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/1/14 下午9:24
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "lodsve.cache", locations = "${params.root}/framework/cache.properties")
public class CacheProperties {
    private EhcacheConfig ehcache = new EhcacheConfig();
    private RedisConfig redis = new RedisConfig();
    private MemcachedConfig memcached = new MemcachedConfig();
    private OscacheConfig oscache = new OscacheConfig();
}

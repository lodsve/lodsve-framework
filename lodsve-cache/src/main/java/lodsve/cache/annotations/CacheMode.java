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

import lodsve.cache.ehcache.EhcacheCacheConfiguration;
import lodsve.cache.memcached.MemcachedCacheConfiguration;
import lodsve.cache.oscache.OscacheCacheConfiguration;
import lodsve.cache.redis.RedisCacheConfiguration;

/**
 * 几种cache.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016/1/19 15:11
 */
public enum CacheMode {
    /**
     * redis cache
     */
    REDIS(RedisCacheConfiguration.class),
    /**
     * ehcache
     */
    EHCAHE(EhcacheCacheConfiguration.class),
    /**
     * memcached
     */
    MEMCACHED(MemcachedCacheConfiguration.class),
    /**
     * oscache
     */
    OSCACHE(OscacheCacheConfiguration.class);

    private final Class<?> cacheConfig;

    CacheMode(Class<?> cacheConfig) {
        this.cacheConfig = cacheConfig;
    }

    public Class<?> getCacheConfig() {
        return cacheConfig;
    }
}

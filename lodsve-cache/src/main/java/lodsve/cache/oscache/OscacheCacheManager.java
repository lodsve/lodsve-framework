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
package lodsve.cache.oscache;

import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import lodsve.cache.properties.OscacheMemcachedCache;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Oscache CacheManager.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-1-10-0010 16:26
 */
public class OscacheCacheManager extends AbstractTransactionSupportingCacheManager {
    private GeneralCacheAdministrator admin;
    private List<OscacheMemcachedCache> cacheConfigs;

    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (!CollectionUtils.isEmpty(cacheConfigs)) {
            for (OscacheMemcachedCache config : cacheConfigs) {
                String name = config.getName();
                int expire = config.getExpire();

                cacheMap.putIfAbsent(config.getName(), new OscacheCache(name, expire, admin));
            }
        }

        super.afterPropertiesSet();
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return cacheMap.values();
    }

    @Override
    public Cache getCache(String name) {
        return cacheMap.get(name);
    }

    public void setAdmin(GeneralCacheAdministrator admin) {
        this.admin = admin;
    }

    public void setCacheConfigs(List<OscacheMemcachedCache> cacheConfigs) {
        this.cacheConfigs = cacheConfigs;
    }
}

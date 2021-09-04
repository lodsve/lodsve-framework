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
package lodsve.cache.ehcache;

import lodsve.cache.properties.CacheProperties;
import lodsve.cache.properties.EhcacheCache;
import lodsve.core.autoproperties.relaxedbind.annotations.EnableConfigurationProperties;
import net.sf.ehcache.Cache;
import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * ehcache.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/4/1 上午9:49
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
public class EhcacheCacheConfiguration {
    private final CacheProperties cacheProperties;

    public EhcacheCacheConfiguration(ObjectProvider<CacheProperties> cacheProperties) {
        this.cacheProperties = cacheProperties.getIfAvailable();
    }

    @Bean
    public CacheManager cacheManager() throws IOException {
        EhCacheCacheManager cacheManager = new EhCacheCacheManager();

        Resource configResource = cacheProperties.getEhcache().getConfiguration();
        if (configResource == null || !configResource.exists()) {
            configResource = new ClassPathResource("/META-INF/ehcache.xml", Thread.currentThread().getContextClassLoader());
        }

        net.sf.ehcache.CacheManager manager = net.sf.ehcache.CacheManager.create(configResource.getInputStream());

        EhcacheCache[] caches = cacheProperties.getEhcache().getCache();

        for (EhcacheCache cache : caches) {
            CacheConfiguration configuration = new CacheConfiguration();
            configuration.setName(cache.getName());
            configuration.setMaxEntriesLocalHeap(cache.getMaxElementsInMemory());
            configuration.setEternal(cache.isEternal());
            configuration.setTimeToIdleSeconds(cache.getTimeToIdleSeconds());
            configuration.setTimeToLiveSeconds(cache.getTimeToLiveSeconds());
            configuration.setOverflowToDisk(cache.isOverflowToDisk());

            manager.addCache(new Cache(configuration));
        }

        cacheManager.setCacheManager(manager);

        return cacheManager;
    }
}

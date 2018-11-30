/*
 * Copyright (C) 2018  Sun.Hao
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.cache.ehcache;

import lodsve.cache.properties.CacheProperties;
import lodsve.cache.properties.EhcacheCache;
import lodsve.core.properties.relaxedbind.annotations.EnableConfigurationProperties;
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
            configuration.setEternal(cache.getEternal());
            configuration.setTimeToIdleSeconds(cache.getTimeToIdleSeconds());
            configuration.setTimeToLiveSeconds(cache.getTimeToLiveSeconds());
            configuration.setOverflowToDisk(cache.getOverflowToDisk());

            manager.addCache(new Cache(configuration));
        }

        cacheManager.setCacheManager(manager);

        return cacheManager;
    }
}

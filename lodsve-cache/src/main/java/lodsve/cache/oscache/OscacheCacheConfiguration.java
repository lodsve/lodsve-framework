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
import lodsve.cache.properties.CacheProperties;
import lodsve.cache.properties.OscacheConfig;
import lodsve.core.autoproperties.relaxedbind.annotations.EnableConfigurationProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 * Oscache Cache Configuration.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-1-10-0010 16:52
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
public class OscacheCacheConfiguration {
    private final CacheProperties cacheProperties;

    public OscacheCacheConfiguration(ObjectProvider<CacheProperties> cacheProperties) {
        this.cacheProperties = cacheProperties.getIfAvailable();
    }

    @Bean
    public CacheManager cacheManager(GeneralCacheAdministrator cacheAdministrator) {
        OscacheConfig oscahce = cacheProperties.getOscache();

        OscacheCacheManager cacheManager = new OscacheCacheManager();
        cacheManager.setAdmin(cacheAdministrator);
        cacheManager.setCacheConfigs(Arrays.asList(oscahce.getCache()));

        return cacheManager;
    }

    @Bean
    public GeneralCacheAdministrator cacheAdministrator() throws IOException {
        OscacheConfig oscahce = cacheProperties.getOscache();
        Resource resource = oscahce.getConfiguration();
        if (resource == null || !resource.exists()) {
            resource = new ClassPathResource("/META-INF/oscache.properties", Thread.currentThread().getContextClassLoader());
        }

        Properties properties = new Properties();
        PropertiesLoaderUtils.fillProperties(properties, resource);

        return new GeneralCacheAdministrator(properties);
    }
}

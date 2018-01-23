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

package lodsve.cache.oscache;

import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import lodsve.cache.properties.CacheProperties;
import lodsve.core.properties.relaxedbind.annotations.EnableConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2018-1-10-0010 16:52
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
public class OscacheCacheConfiguration {
    @Autowired
    private CacheProperties cacheProperties;

    @Bean
    public CacheManager cacheManager(GeneralCacheAdministrator cacheAdministrator) {
        CacheProperties.Oscahce oscahce = cacheProperties.getOscahce();

        OscacheCacheManager cacheManager = new OscacheCacheManager();
        cacheManager.setAdmin(cacheAdministrator);
        cacheManager.setCacheConfigs(Arrays.asList(oscahce.getCache()));

        return cacheManager;
    }

    @Bean
    public GeneralCacheAdministrator cacheAdministrator() throws IOException {
        CacheProperties.Oscahce oscahce = cacheProperties.getOscahce();
        Resource resource = oscahce.getConfiguration();
        if (resource == null || !resource.exists()) {
            resource = new ClassPathResource("/META-INF/oscache.properties", Thread.currentThread().getContextClassLoader());
        }

        Properties properties = new Properties();
        PropertiesLoaderUtils.fillProperties(properties, resource);

        return new GeneralCacheAdministrator(properties);
    }
}

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

package lodsve.cache.guava;

import lodsve.cache.properties.CacheProperties;
import lodsve.core.properties.autoconfigure.annotations.EnableConfigurationProperties;
import lodsve.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * guava.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/4/1 上午9:49
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
public class GuavaCacheConfiguration {
    @Autowired
    private CacheProperties cacheProperties;

    @Bean
    public CacheManager cacheManager() {
        GuavaCacheManager cacheManager = new GuavaCacheManager();

        CacheProperties.Guava guava = cacheProperties.getGuava();
        String cacheNames = guava.getCacheNames();
        List<String> cacheNameList = Arrays.asList(StringUtils.split(cacheNames));

        if (!cacheNameList.isEmpty()) {
            cacheManager.setCacheNames(cacheNameList);
        }

        cacheManager.setCacheSpecification(guava.getSpec());

        return cacheManager;
    }
}

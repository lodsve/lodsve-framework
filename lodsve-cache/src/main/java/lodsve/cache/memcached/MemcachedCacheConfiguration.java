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

package lodsve.cache.memcached;

import lodsve.cache.properties.CacheProperties;
import lodsve.cache.properties.MemcachedConfig;
import lodsve.core.properties.relaxedbind.annotations.EnableConfigurationProperties;
import lodsve.core.utils.NumberUtils;
import net.spy.memcached.MemcachedClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Memcached Cache Configuration.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2018-1-10-0010 10:35
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
public class MemcachedCacheConfiguration {
    @Autowired
    private CacheProperties cacheProperties;

    @Bean
    public CacheManager cacheManager(MemcachedClient client) {
        MemcachedConfig memcached = cacheProperties.getMemcached();

        MemcachedCacheManager cacheManager = new MemcachedCacheManager();
        cacheManager.setMemcachedClient(client);
        cacheManager.setCacheConfigs(Arrays.asList(memcached.getCache()));

        return cacheManager;
    }

    @Bean
    public MemcachedClient memcachedClient() throws IOException {
        MemcachedConfig memcached = cacheProperties.getMemcached();
        String server = memcached.getServer();
        String[] servers = StringUtils.delimitedListToStringArray(server, ",");
        List<InetSocketAddress> addressList = new ArrayList<>(servers.length);
        for (String s : servers) {
            if (StringUtils.isEmpty(s)) {
                continue;
            }

            InetSocketAddress address;
            String[] temp = StringUtils.delimitedListToStringArray(s, ":");
            if (temp.length == 1) {
                address = new InetSocketAddress(temp[0], 0);
            } else if (temp.length == 2) {
                if (!NumberUtils.isNumber(temp[1])) {
                    continue;
                }
                address = new InetSocketAddress(temp[0], NumberUtils.toInt(temp[1]));
            } else {
                continue;
            }

            addressList.add(address);
        }

        return new MemcachedClient(addressList);
    }
}

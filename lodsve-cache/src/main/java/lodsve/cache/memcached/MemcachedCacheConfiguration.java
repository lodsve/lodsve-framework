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
package lodsve.cache.memcached;

import lodsve.cache.properties.CacheProperties;
import lodsve.cache.properties.MemcachedConfig;
import lodsve.core.autoproperties.relaxedbind.annotations.EnableConfigurationProperties;
import lodsve.core.utils.NumberUtils;
import net.spy.memcached.MemcachedClient;
import org.springframework.beans.factory.ObjectProvider;
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
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-1-10-0010 10:35
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
public class MemcachedCacheConfiguration {
    private final CacheProperties cacheProperties;

    public MemcachedCacheConfiguration(ObjectProvider<CacheProperties> cacheProperties) {
        this.cacheProperties = cacheProperties.getIfAvailable();
    }

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
                if (!NumberUtils.isCreatable(temp[1])) {
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

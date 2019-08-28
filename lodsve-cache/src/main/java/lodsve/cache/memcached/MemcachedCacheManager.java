/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
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

import lodsve.cache.properties.OscacheMemcachedCache;
import net.spy.memcached.MemcachedClient;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Memcached CacheManager.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-1-10-0010 09:58
 */
public class MemcachedCacheManager extends AbstractTransactionSupportingCacheManager {
    private MemcachedClient memcachedClient;
    private List<OscacheMemcachedCache> cacheConfigs;

    private ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (!CollectionUtils.isEmpty(cacheConfigs)) {
            for (OscacheMemcachedCache config : cacheConfigs) {
                String name = config.getName();
                int expire = config.getExpire();

                cacheMap.putIfAbsent(config.getName(), new MemcachedCache(name, expire, memcachedClient));
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

    public void setMemcachedClient(MemcachedClient memcachedClient) {
        this.memcachedClient = memcachedClient;
    }

    public void setCacheConfigs(List<OscacheMemcachedCache> cacheConfigs) {
        this.cacheConfigs = cacheConfigs;
    }
}

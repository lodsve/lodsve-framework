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

    private Class<?> cacheConfig;

    CacheMode(Class<?> cacheConfig) {
        this.cacheConfig = cacheConfig;
    }

    public Class<?> getCacheConfig() {
        return cacheConfig;
    }
}

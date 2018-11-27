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

package lodsve.cache.properties;

import lodsve.core.properties.relaxedbind.annotations.ConfigurationProperties;

/**
 * cache配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/1/14 下午9:24
 */
@ConfigurationProperties(prefix = "lodsve.cache", locations = "${params.root}/framework/cache.properties")
public class CacheProperties {
    private EhcacheConfig ehcache = new EhcacheConfig();
    private RedisConfig redis = new RedisConfig();
    private MemcachedConfig memcached = new MemcachedConfig();
    private OscahceConfig oscahce = new OscahceConfig();

    public EhcacheConfig getEhcache() {
        return ehcache;
    }

    public void setEhcache(EhcacheConfig ehcache) {
        this.ehcache = ehcache;
    }

    public RedisConfig getRedis() {
        return redis;
    }

    public void setRedis(RedisConfig redis) {
        this.redis = redis;
    }

    public MemcachedConfig getMemcached() {
        return memcached;
    }

    public void setMemcached(MemcachedConfig memcached) {
        this.memcached = memcached;
    }

    public OscahceConfig getOscahce() {
        return oscahce;
    }

    public void setOscahce(OscahceConfig oscahce) {
        this.oscahce = oscahce;
    }
}

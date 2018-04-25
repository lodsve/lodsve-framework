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

import org.springframework.core.io.Resource;

/**
 * Oscahce Config.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @date 2018-4-25-0025 11:45
 */
public class OscahceConfig {
    private Resource configuration;
    private OscacheMemcachedCache[] cache = new OscacheMemcachedCache[]{new OscacheMemcachedCache()};

    public Resource getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Resource configuration) {
        this.configuration = configuration;
    }

    public OscacheMemcachedCache[] getCache() {
        return cache;
    }

    public void setCache(OscacheMemcachedCache[] cache) {
        this.cache = cache;
    }
}

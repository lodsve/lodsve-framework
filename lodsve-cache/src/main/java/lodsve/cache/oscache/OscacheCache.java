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

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * Oscache Cache.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2018-1-10-0010 16:27
 */
public class OscacheCache extends AbstractValueAdaptingCache {
    private final String name;
    private final GeneralCacheAdministrator admin;
    private final int expire;

    public OscacheCache(String name, int expire, GeneralCacheAdministrator admin) {
        super(true);
        this.name = name;
        this.admin = admin;
        this.expire = expire;
    }

    @Override
    protected Object lookup(Object key) {
        Object value;
        try {
            value = admin.getFromCache(key.toString(), expire);
        } catch (NeedsRefreshException e) {
            value = null;
        }
        return value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public GeneralCacheAdministrator getNativeCache() {
        return admin;
    }

    @Override
    public void put(Object key, Object value) {
        admin.putInCache(key.toString(), value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        if (value == null) {
            return new SimpleValueWrapper(null);
        }

        ValueWrapper wrapper = get(key);
        if (wrapper != null) {
            return wrapper;
        }

        put(key, value);
        return new SimpleValueWrapper(value);
    }

    @Override
    public void evict(Object key) {
        admin.cancelUpdate(key.toString());
    }

    @Override
    public void clear() {
        admin.flushAll();
    }
}

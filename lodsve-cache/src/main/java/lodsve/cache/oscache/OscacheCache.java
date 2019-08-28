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

package lodsve.cache.oscache;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.lang.NonNull;

import java.util.concurrent.Callable;

/**
 * Oscache Cache.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-1-10-0010 16:27
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
    @SuppressWarnings("unchecked")
    public <T> T get(@NonNull Object key, @NonNull Callable<T> valueLoader) {
        Object object = lookup(key);
        if (null != object) {
            return (T) object;
        } else {
            object = lookup(key);

            if (null != object) {
                return (T) object;
            } else {
                return loadValue(key, valueLoader);
            }
        }
    }

    private <T> T loadValue(Object key, Callable<T> valueLoader) {
        T value;
        try {
            value = valueLoader.call();
        } catch (Throwable ex) {
            throw new ValueRetrievalException(key, valueLoader, ex);
        }
        put(key, value);
        return value;
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

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

import net.spy.memcached.MemcachedClient;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.lang.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Memcached Cache.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-1-10-0010 10:06
 */
public class MemcachedCache extends AbstractValueAdaptingCache {
    private final String name;
    private final MemcachedClient memcachedClient;
    private final int expire;

    public MemcachedCache(String name, int expire, MemcachedClient memcachedClient) {
        super(true);
        this.name = name;
        this.memcachedClient = memcachedClient;
        this.expire = expire;
    }

    @Override
    protected Object lookup(Object key) {
        Object value = null;
        Future future = memcachedClient.asyncGet(decorateKey(key));
        try {
            value = future.get(expire, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            future.cancel(true);
        }

        return value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public MemcachedClient getNativeCache() {
        return memcachedClient;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(@NonNull Object key, @NonNull Callable<T> valueLoader) {
        Object object = lookup(key);
        if (null == object) {
            object = lookup(key);

            if (null != object) {
                return (T) object;
            } else {
                T value;
                try {
                    value = valueLoader.call();
                } catch (Exception ex) {
                    throw new ValueRetrievalException(key, valueLoader, ex);
                }

                put(key, value);
                return value;
            }
        } else {
            return (T) object;
        }
    }

    @Override
    public void put(Object key, Object value) {
        memcachedClient.add(decorateKey(key), expire, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        if (value == null) {
            return new SimpleValueWrapper(null);
        }

        Object object = lookup(key);
        if (object != null) {
            return new SimpleValueWrapper(object);
        }

        put(key, value);
        return new SimpleValueWrapper(value);
    }

    @Override
    public void evict(Object key) {
        memcachedClient.delete(decorateKey(key));
    }

    @Override
    public void clear() {
        memcachedClient.flush();
    }

    private String decorateKey(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("key can't be null!");
        }
        return name + "_" + key.toString();
    }
}

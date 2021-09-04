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

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

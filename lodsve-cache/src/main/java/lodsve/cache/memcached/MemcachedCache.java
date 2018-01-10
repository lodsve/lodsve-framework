package lodsve.cache.memcached;

import net.spy.memcached.MemcachedClient;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Memcached Cache.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2018-1-10-0010 10:06
 */
public class MemcachedCache implements Cache {
    private final String name;
    private final MemcachedClient memcachedClient;
    private final int expire;

    public MemcachedCache(String name, int expire, MemcachedClient memcachedClient) {
        this.name = name;
        this.memcachedClient = memcachedClient;
        this.expire = expire;
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
    public ValueWrapper get(Object key) {
        ValueWrapper wrapper = null;
        Object value = memcachedClient.get(decorateKey(key));
        if (value != null) {
            wrapper = new SimpleValueWrapper(value);
        }
        return wrapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Class<T> type) {
        T value;
        Future future = memcachedClient.asyncGet(decorateKey(key));
        try {
            value = (T) future.get(expire, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            future.cancel(true);
            value = null;
        }

        if (value != null && type != null && !type.isInstance(value)) {
            throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
        }

        return value;
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

        ValueWrapper wrapper = get(key);
        if (wrapper != null) {
            return wrapper;
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

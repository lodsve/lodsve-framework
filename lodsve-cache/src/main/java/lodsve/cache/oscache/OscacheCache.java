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

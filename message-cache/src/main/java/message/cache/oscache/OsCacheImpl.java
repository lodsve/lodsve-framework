package message.cache.oscache;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import message.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * oscache cache impl.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 13-3-30 上午4:57
 */
public class OsCacheImpl implements Cache {
    private static final Logger logger = LoggerFactory.getLogger(OsCacheImpl.class);

    /**oscache general cache administrator**/
    private GeneralCacheAdministrator admin;
    /**default timeout, how long can this object stay in cache by second!**/
    private long defaultTimeout;

    public OsCacheImpl(GeneralCacheAdministrator admin, long defaultTimeout) {
        this.admin = admin;
        this.defaultTimeout = defaultTimeout;
    }

    @Deprecated
    public List getKeys() {
        return Collections.EMPTY_LIST;
    }

    public Object put(String key, Object value) {
        this.admin.putInCache(key, value);
        return value;
    }

    public Object put(String key, Object value, int expire) {
        this.admin.putInCache(key, value);
        return value;
    }

    public void remove(String key) {
        this.admin.cancelUpdate(key);
    }

    public Object get(String key) {
        try {
            return this.admin.getFromCache(key, (int) this.defaultTimeout);
        } catch (NeedsRefreshException e) {
            logger.error("get cache object for key '{}' append exception:'{}'!", key, e);
            return null;
        }
    }

    public List get(String[] keys) {
        if(keys == null || keys.length < 0){
            logger.error("given null keys!");
            return Collections.EMPTY_LIST;
        }
        List<Object> objs = new ArrayList<Object>(keys.length);
        for(String key : keys){
            Object obj = this.get(key);
            if(obj != null)
                objs.add(obj);
        }

        return objs;
    }

    public void remove(String[] keys) {
        if(keys == null || keys.length < 0){
            logger.error("given null keys!");
            return;
        }

        for(String key : keys){
            this.remove(key);
        }
    }

    public void removeAll() {
        this.admin.flushAll();
    }

    public void flush() {
        this.admin.flushAll();
    }
}

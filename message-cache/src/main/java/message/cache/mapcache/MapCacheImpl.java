package message.cache.mapcache;

import message.base.utils.StringUtils;
import message.cache.Cache;
import message.cache.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * use map for cache
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 12-4-9 上午8:31
 */
public class MapCacheImpl implements Cache {
	private Map cacheMap = new HashMap(2000);

    public List getKeys() {
        List keys = new ArrayList();
        Iterator it = this.cacheMap.keySet().iterator();
        while(it.hasNext())
            keys.add(it.next());

        return keys;
    }

    public Object put(String key, Object value) {
        return put(key, value, Constants.DEFAULT_EXPIRE_TIME);
    }

    public Object put(String key, Object value, int expire) {
        if(StringUtils.isEmpty(key) || value == null)
            return null;

        MapCacheObject obj = new MapCacheObject(value, System.currentTimeMillis(), expire);
        this.cacheMap.put(key, obj);
        return value;
    }

    public void remove(String key) {
        this.cacheMap.remove(key);
    }

    public Object get(String key) {
        MapCacheObject obj = (MapCacheObject) this.cacheMap.get(key);
        if(obj == null)
            return null;

        if(obj.isActive())
            //if active, return it
            return obj.getValue();

        //else remove it
        this.cacheMap.remove(key);
        return null;
    }

    public List get(String[] keys) {
        if(keys == null || keys.length < 1)
            return null;

        int length = keys.length;
        List result = new ArrayList(length);
        for(String key : keys){
            Object obj = get(key);
            if(obj != null)
                result.add(obj);
        }

        return result;
    }

    public void remove(String[] keys) {
        if(keys == null || keys.length < 1)
            return;

        for(String key : keys)
            remove(key);
    }

    public void removeAll() {
        this.cacheMap.clear();
    }

    public void flush() {
        this.cacheMap.clear();
    }
}

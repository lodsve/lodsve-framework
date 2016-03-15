package message.cache.mapcache;

import message.base.utils.StringUtils;
import message.cache.Cache;
import message.cache.CacheManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * use map for cache
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 12-4-9 上午8:22
 */
public class MapCacheManagerImpl implements CacheManager {
	private Map cacheMap = Collections.synchronizedMap(new LinkedHashMap(200));

    public List getCacheNames() {
        List names = new ArrayList();
        Iterator it = this.cacheMap.keySet().iterator();
        while(it.hasNext()){
            names.add(it.next());
        }
        return names;
    }

    public Cache getCache(String region) {
        if(StringUtils.isEmpty(region))
            return null;

        Cache cache = (Cache) this.cacheMap.get(region);
        if(cache == null){
            cache = new MapCacheImpl();

            this.cacheMap.put(region, cache);
        }
        return cache;
    }

    public void removeCache(String region) {
        this.cacheMap.remove(region);
    }

    public void flush() {
        this.cacheMap.clear();
    }

    public void destroy() throws Exception {

    }

    public void afterPropertiesSet() throws Exception {

    }
}

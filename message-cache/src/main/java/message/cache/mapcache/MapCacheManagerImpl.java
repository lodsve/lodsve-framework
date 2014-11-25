package message.cache.mapcache;

import message.cache.Cache;
import message.cache.CacheManager;
import message.utils.StringUtils;

import java.util.*;

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

package message.cache.utils;

import message.cache.Cache;

import java.io.Serializable;

/**
 * cache object
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 12-4-9 上午9:34
 */
public class ObjectCache {
    private static final String KEY_SEPARATOR = "#";
    private Cache cache;

    /**
     * put object into cache
     *
     * @param object        object need to be cache
     * @param pkValue       value of object pkId
     */
    public void put(Object object, Serializable pkValue){
        if(object != null)
            this.cache.put(generateCacheKey(object.getClass(), pkValue), object);
    }

    /**
     * get cache object from cache
     * 
     * @param clazz         cache object class name
     * @param pkValue       cache object pkId
     * @return
     */
	public Object get(Class clazz, Serializable pkValue){
        return this.cache.get(generateCacheKey(clazz, pkValue));
    }

    /**
     * get cache object from cahce by pkId
     *
     * @param clazz         cache object class name
     * @param pkValue       cache object pkId
     */
    public void remove(Class clazz, Serializable pkValue){
        if(pkValue != null)
            this.cache.remove(generateCacheKey(clazz, pkValue));
    }

    /**
     * remove all cache
     */
    public void removeAll(){
        this.cache.removeAll();
    }

    /**
     * create cache key by cache object class name and pkId
     * 
     * @param clazz         cache object class name
     * @param pkValue       cache object pkId
     * @return
     */
    private String generateCacheKey(Class clazz, Serializable pkValue) {
        //将缓存用作的键设成缓存对象的类名 + 对象主键的类名 + 主键值
        StringBuffer cacheKey = new StringBuffer();
        cacheKey.append(clazz.getName()).append(KEY_SEPARATOR).append(pkValue.getClass().getName()).append(KEY_SEPARATOR).append(pkValue);

        return cacheKey.toString();
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }
}

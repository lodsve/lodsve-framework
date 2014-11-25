package message.cache;

import java.util.List;

/**
 * cache
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 12-4-9 上午6:49
 */
public interface Cache {

    /**
     * get all of the keys as list
     * @return
     */
    List getKeys();

    /**
     * put object into cache as key-value
     * default active time is 30 days
     * 
     * @param key       the key
     * @param value     the value
     * @return
     */
    Object put(String key, Object value);

    /**
     * put object into cache as key-value with given active time
     * 
     * @param key       the key
     * @param value     the value
     * @param expire    active time(second)
     * @return
     */
    Object put(String key, Object value, int expire);

    /**
     * remove the cache which mapped given key
     * @param key       cache's key
     */
    void remove(String key);

    /**
     * get cache with given key
     * 
     * @param key       cache's key
     * @return
     */
    Object get(String key);

    /**
     * get cache as list with given keys
     * @param keys      caches keys
     * @return
     */
    List get(String[] keys);

    /**
     * remove cache as list with given keys
     * @param keys      cache's key
     */
    void remove(String[] keys);

    /**
     * remove all cache
     */
    void removeAll();

    /**
     * flush all cache
     */
    void flush();
}

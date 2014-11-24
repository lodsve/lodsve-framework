package message.cache.ehcache;

import message.cache.Cache;
import message.cache.Constants;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * eh cache implement
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 12-4-9 上午7:10
 */
public class EHCacheImpl implements Cache {
    private static final Logger logger = LoggerFactory.getLogger(EHCacheImpl.class);

    private net.sf.ehcache.Cache cache;

    /**
     * constructor
     * @param cache
     */
    public EHCacheImpl(net.sf.ehcache.Cache cache){
        this.cache = cache;
    }

	public List getKeys() {
        return this.cache.getKeys();
    }

    public Object put(String key, Object value) {
        return put(key, value, Constants.DEFAULT_EXPIRE_TIME);
    }

    public Object put(String key, Object value, int expire) {
        Element element = new Element(key, value);
        //set unactive time
        element.setTimeToIdle(expire);

        this.cache.put(element);
        return value;
    }

    public void remove(String key) {
        logger.debug("given key is '{}'", key);
        this.cache.remove(key);
    }

    public Object get(String key) {
        Element element = this.cache.get(key);
        if(element == null)
            return null;

        return element.getValue();
    }

	public List get(String[] keys) {
        if(keys == null || keys.length < 1) {
            logger.warn("given keys is null!");
            return null;
        }

        int length = keys.length;
        logger.info("given keys length is '{}'", length);
        List result = new ArrayList(length);
        for(String key : keys){
            Object obj = get(key);
            if(obj != null)
                result.add(obj);
        }

        return result;
    }

    public void remove(String[] keys) {
        if(keys == null || keys.length < 1){
            logger.warn("given keys is null!");
            return;
        }

        for(String key : keys)
            remove(key);
    }

    public void removeAll() {
        this.cache.removeAll();
    }

    public void flush() {
        this.cache.removeAll();
    }
}

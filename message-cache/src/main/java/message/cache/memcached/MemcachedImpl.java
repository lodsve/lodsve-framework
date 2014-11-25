package message.cache.memcached;

import message.cache.Cache;
import message.utils.StringUtils;
import net.spy.memcached.MemcachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * memcached cache 实现.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 13-3-25 下午7:43
 */
public class MemcachedImpl implements Cache {
    private static final Logger logger = LoggerFactory.getLogger(MemcachedImpl.class);

    /**memcached连接服务端的客户端工具,指定服务端的地址以及端口**/
    private MemcachedClient mcc;
    /**默认的域**/
    private String region = "DEFAULT#REGION";
    /**默认缓存存活时间**/
    private long defaultTimeout;
    /**从服务端取缓存对象超出这个时间就放弃从服务端取缓存对象**/
    private long cacheGetTimeout;

    public MemcachedImpl(MemcachedClient mcc, String region, long defaultTimeout, long cacheGetTimeout) {
        this.mcc = mcc;
        this.region = region;
        this.defaultTimeout = defaultTimeout;
        this.cacheGetTimeout = cacheGetTimeout;
    }

    @Deprecated
    public List getKeys() {
        return Collections.EMPTY_LIST;
    }

    public Object put(String key, Object value) {
        return this.put(key, value, (int) this.defaultTimeout);
    }

    public Object put(String key, Object value, int expire) {
        this.mcc.add(this.getKey(key), expire, value);
        return value;
    }

    public void remove(String key) {
        if(StringUtils.isEmpty(key)){
            logger.error("given null key!");
            return;
        }
        this.mcc.delete(this.getKey(key));
    }

    public Object get(String key) {
        Object obj = null;
        Future future = this.mcc.asyncGet(this.getKey(key));
        try {
            obj = future.get(this.cacheGetTimeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            future.cancel(true);
        }

        return obj;
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

    @Deprecated
    public void removeAll() {
        logger.debug("memcached has no removeAll method!");
    }

    public void flush() {
        this.mcc.flush();
    }

    private String getKey(String key){
        return this.region + "@" + key;
    }

    public MemcachedClient getMcc() {
        return mcc;
    }

    public void setMcc(MemcachedClient mcc) {
        this.mcc = mcc;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public long getDefaultTimeout() {
        return defaultTimeout;
    }

    public void setDefaultTimeout(long defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    public long getCacheGetTimeout() {
        return cacheGetTimeout;
    }

    public void setCacheGetTimeout(long cacheGetTimeout) {
        this.cacheGetTimeout = cacheGetTimeout;
    }
}

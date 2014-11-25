package message.cache;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * cache manager
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 12-4-9 上午6:47
 */
public interface CacheManager extends InitializingBean, DisposableBean {

    /**
     * get all cache names
     * 
     * @return
     */
	List getCacheNames();

    /**
     * get cache with cache name
     * 
     * @param region    cache scope
     * @return
     */
    Cache getCache(String region);

    /**
     * remove cache with given cache name
     * 
     * @param region    cache scope
     */
    void removeCache(String region);

    /**
     * flush the cache
     */
    void flush();

}

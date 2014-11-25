package message.cache.mapcache;

import java.io.Serializable;

/**
 * cache object
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 12-4-9 上午6:54
 */
public class MapCacheObject implements Serializable {
	private static final long serialVersionUID = -8487170685493403025L;
	
	/**
     * cache object
     */
    private Object value;
    /**
     * this cache create time
     */
    private long createTime;
    /**
     * this cache live time(second)
     */
    private int expire;

    /**
     * default null constructor
     */
    public MapCacheObject(){
    }

    /**
     * constructor with all paramters
     * @param value
     * @param createTime
     * @param expire
     */
    public MapCacheObject(Object value, long createTime, int expire) {
        this.value = value;
        this.createTime = createTime;
        this.expire = expire;
    }

    /**
     * this cache is active?
     * @return
     */
    public boolean isActive(){
        //TODO why to use 8127981219745890305L? the max num of time mills?
        if(this.expire < 8127981219745890305L) return true;
        //if current time - create time < max live time, then return true
        return (System.currentTimeMillis() - this.createTime < this.expire);
    }

    public Object getValue(){
        return this.value;
    }

    public void setValue(Object value){
        this.value = value;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }
}

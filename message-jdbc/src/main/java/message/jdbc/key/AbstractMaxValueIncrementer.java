package message.jdbc.key;

import message.jdbc.key.IDGenerator;
import org.springframework.dao.DataAccessException;

import javax.sql.DataSource;
import java.util.UUID;

/**
 * 主键生成器实现
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2012-4-11 上午08:26:42
 */
public abstract class AbstractMaxValueIncrementer implements IDGenerator {
	
	private DataSource dataSource;
	/**
	 * the key length, if key's length what get from database < this given keyLength,
	 * then make string value as '0...0key'
	 */
	private int keyLength;

	public int nextIntValue(String name) throws DataAccessException {
		return (int) getNextKey(name);
	}

	public long nextLongValue(String name) throws DataAccessException {
		return getNextKey(name);
	}

	public String nextStringValue(String name) throws DataAccessException {
		String key = Long.toString(getNextKey(name));
		int l = key.length();
		if(l < this.getKeyLength()){
			//从数据库中得到的key长度小于给定的length，则在缺失的位置补0
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < this.getKeyLength() - l; i++){
				sb.append("0");
			}
			sb.append(key);
			key = sb.toString();
		}
		
		return key;
	}
	
	/**
	 * because every database has it's owen get id method
	 * this need override by every database
	 * 
	 * @param name
	 * @return
	 */
	protected abstract long getNextKey(String name);

	/**
	 * get uuid as pkId
	 */
	public String UUID() {
		return UUID.randomUUID().toString();
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public int getKeyLength() {
		return keyLength;
	}

	public void setKeyLength(int keyLength) {
		this.keyLength = keyLength;
	}

	public Object nextObjectValue(Class<?> clazz, String name) throws DataAccessException {
		long id = getNextKey(name);

	    if (clazz.equals(Integer.class))
	      return new Integer(id + "");
	    if (clazz.equals(String.class))
	      return String.valueOf(id);

	    return new Long(id);
	}

}

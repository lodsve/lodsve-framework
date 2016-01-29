package message.mybatis.key;

import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;

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

	public long nextLongValue(String name) throws DataAccessException {
		return getNextKey(name);
	}
	
	/**
	 * because every database has it's owen get id method
	 * this need override by every database
	 * 
	 * @param name
	 * @return
	 */
	protected abstract long getNextKey(String name);
	
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
}

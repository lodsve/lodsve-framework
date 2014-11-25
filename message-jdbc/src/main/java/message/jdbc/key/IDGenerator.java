package message.jdbc.key;

import org.springframework.dao.DataAccessException;

import javax.sql.DataSource;

/**
 * make database id
 * oracle:sequence	mysql:auto
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2012-4-11 上午08:10:32
 */
public interface IDGenerator {
	/**
	 * get next int value
	 * 
	 * @param name
	 * @return
	 * @throws org.springframework.dao.DataAccessException
	 */
	public int nextIntValue(String name) throws DataAccessException;

	/**
	 * get next long value
	 * 
	 * @param name
	 * @return
	 * @throws org.springframework.dao.DataAccessException
	 */
    public long nextLongValue(String name) throws DataAccessException;

    /**
     * get next string value
     * 
     * @param name
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public String nextStringValue(String name) throws DataAccessException;

    /**
     * get uuid
     * 
     * @return
     */
    public String UUID();
    
    /**
     * get next object value
     * 
     * @param paramClass
     * @param paramString
     * @return
     * @throws DataAccessException
     */
    public abstract Object nextObjectValue(Class<?> paramClass, String paramString) throws DataAccessException;

    /**
     * set datasource
     * 
     * @param dataSource
     */
    public void setDataSource(DataSource dataSource);
}

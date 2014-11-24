package message.jdbc.key.impl.generic;

import message.jdbc.key.impl.AbstractMaxValueIncrementer;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * SQL Server主键自增长策略
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2012-4-11 上午10:40:59
 */
public class SqlServerMaxValueIncrementer extends AbstractMaxValueIncrementer {

	/** The current cache of values */
	private long[] valueCache;

	/** The next id to serve from the value cache */
	private int nextValueIndex = -1;
	
	/** The number of keys buffered in a cache */
	private int cacheSize = 1;

	protected synchronized long getNextKey(String name) throws DataAccessException {
		if (this.nextValueIndex < 0 || this.nextValueIndex >= getCacheSize()) {
			/*
			* Need to use straight JDBC code because we need to make sure that the insert and select
			* are performed on the same connection (otherwise we can't be sure that @@identity
			* returnes the correct value)
			*/
			Connection con = DataSourceUtils.getConnection(getDataSource());
			Statement stmt = null;
			try {
				stmt = con.createStatement();
				DataSourceUtils.applyTransactionTimeout(stmt, getDataSource());
				this.valueCache = new long[getCacheSize()];
				this.nextValueIndex = 0;
				for (int i = 0; i < getCacheSize(); i++) {
					stmt.executeUpdate("insert into " + name + " default values");
					ResultSet rs = stmt.executeQuery("select @@identity");
					try {
						if (!rs.next()) {
							throw new DataAccessResourceFailureException("@@identity failed after executing an update");
						}
						this.valueCache[i] = rs.getLong(1);
					}
					finally {
						JdbcUtils.closeResultSet(rs);
					}
				}
				long maxValue = this.valueCache[(this.valueCache.length - 1)];
				stmt.executeUpdate("delete from " +name + " where " + name + " < " + maxValue);
			}
			catch (SQLException ex) {
				throw new DataAccessResourceFailureException("Could not increment identity", ex);
			}
			finally {
				JdbcUtils.closeStatement(stmt);
				DataSourceUtils.releaseConnection(con, getDataSource());
			}
		}
		return this.valueCache[this.nextValueIndex++];
	}
	
	/**
	 * Return the number of buffered keys.
	 */
	public int getCacheSize() {
		return cacheSize;
	}

	/**
	 * Set the number of buffered keys.
	 */
	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}

}

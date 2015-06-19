package message.jdbc.key.sequence;

import message.jdbc.key.AbstractMaxValueIncrementer;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 所有利用sequence实现主键自增长的数据库均需继承此类，并实现getQuerySequence方法
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2012-4-11 上午08:45:39
 */
public abstract class AbstractSequenceMaxValueIncrementer extends AbstractMaxValueIncrementer {
	protected long getNextKey(String name) {
		Connection conn = DataSourceUtils.getConnection(getDataSource());
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			DataSourceUtils.applyTransactionTimeout(stmt, getDataSource());
            String sql = getQuerySequence(name);
			rs = stmt.executeQuery(sql);

			if(rs.next())
				return rs.getLong(1);
			else
				throw new DataAccessResourceFailureException("can not get any return from sequence '" + name + "' you give");
			
		} catch(SQLException e) {
			throw new DataAccessResourceFailureException("not found sequence you give '" + name + "'");
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(stmt);
			DataSourceUtils.releaseConnection(conn, getDataSource());
		}
	}
	
	/**
	 * query next sequence sql
	 * 
	 * @param name		sequence name
	 * @return
	 */
	public abstract String getQuerySequence(String name);

}

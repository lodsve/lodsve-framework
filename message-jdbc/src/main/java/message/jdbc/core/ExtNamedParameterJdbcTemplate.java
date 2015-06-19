package message.jdbc.core;

import message.utils.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.core.namedparam.ParsedSql;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * 扩展的NamedParameterJdbcTemplate
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2012-4-10 上午12:27:48
 */
public class ExtNamedParameterJdbcTemplate extends NamedParameterJdbcTemplate {

	public ExtNamedParameterJdbcTemplate(DataSource dataSource) {
		super(dataSource);
	}
	
	public ExtNamedParameterJdbcTemplate(JdbcOperations jdbcOperations) {
		super(jdbcOperations);
	}
	
	public int[] updateBatch(String sql, final List paramSource) throws Exception {
		if(StringUtils.isEmpty(sql) || paramSource.isEmpty())
			return null;
		
		ParsedSql parsedSql = getParsedSql(sql);
		String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, (SqlParameterSource) paramSource.get(0));
		int paramTypes[] = NamedParameterUtils.buildSqlTypeArray(parsedSql, (SqlParameterSource) paramSource.get(0));
		PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(sqlToUse, paramTypes);
		
		Iterator it = paramSource.iterator();
		final PreparedStatementSetter setter[] = new PreparedStatementSetter[paramSource.size()];
		int i = 0;
		while(it.hasNext()) {
			Object params[] = NamedParameterUtils.buildValueArray(parsedSql, (SqlParameterSource) it.next(), null);
			PreparedStatementSetter s = factory.newPreparedStatementSetter(params);
			setter[i] = s;
			i++;
		}
		
		return this.getJdbcOperations().batchUpdate(sqlToUse, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				setter[i].setValues(ps);
			}
			
			public int getBatchSize() {
				return setter.length;
			}
		});
	}
	
	public int updateByBean(String sql, final Object bean) throws DataAccessException {
		if(bean == null)
			return -1;
		
		return 0;
	}

}

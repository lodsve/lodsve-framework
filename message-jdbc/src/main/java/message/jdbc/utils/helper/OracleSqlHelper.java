package message.jdbc.utils.helper;

import message.base.utils.ApplicationHelper;
import message.jdbc.key.impl.sequence.OracleSequenceMaxValueIncrementer;
import message.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.lob.LobHandler;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * oracle sql helper implement
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2012-4-10 上午07:16:41
 */
public class OracleSqlHelper extends SqlHelper {
	private static final Logger logger = LoggerFactory.getLogger(OracleSqlHelper.class);
	
	private LobHandler lobHandler;

	public String getPageSql(String sql, int start, int num) {
		if(start < 0 || num < 0)
			return sql;
		
		StringBuffer pageSql = new StringBuffer(" SELECT * FROM ( ");
		pageSql.append(" SELECT temp.* ,ROWNUM num FROM ( ");
		pageSql.append(sql);
        int last = start + num;
        pageSql.append(" ) temp where ROWNUM <= ").append(last);
        pageSql.append(" ) WHERE num > ").append(start);

        logger.debug("page sql:" + pageSql.toString());
		
		return pageSql.toString();
	}

    public Object getNextId(String sequenceName) {
        if(StringUtils.isEmpty(sequenceName)){
            logger.warn("given null sequenceName!");
            return null;
        }

        OracleSequenceMaxValueIncrementer oracleIdGenerator = null;
        if(super.idGenerator != null && super.idGenerator instanceof OracleSequenceMaxValueIncrementer)
            oracleIdGenerator = (OracleSequenceMaxValueIncrementer) super.idGenerator;
        else
            oracleIdGenerator = ApplicationHelper.getInstance().getBean(OracleSequenceMaxValueIncrementer.class);

        if(oracleIdGenerator == null){
            logger.error("can not get any OracleSequenceMaxValueIncrementer!");
            return null;
        }

        String nextId = oracleIdGenerator.nextStringValue(sequenceName);

        return nextId;
    }

    public String existTableSQL(String tableName, DataSource dataSource) throws Exception {
        String sql = "SELECT COUNT(TABLE_NAME) FROM USER_TABLES WHERE TABLE_NAME = '" + tableName.toUpperCase() + "'";

        return sql;
    }

    public void setClobStringVlaue(PreparedStatement ps, int index, String value)
			throws SQLException {
		this.lobHandler.getLobCreator().setClobAsString(ps, index, value);
	}

	public String getClobStringValue(ResultSet rs, int index)
			throws SQLException {
		return this.lobHandler.getClobAsString(rs, index);
	}

	public LobHandler getLobHandler() {
		return lobHandler;
	}

	public void setLobHandler(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}

}

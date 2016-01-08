package message.mybatis.helper;

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
public class OracleSqlHelper extends AbstractSqlHelper {
    private static final Logger logger = LoggerFactory.getLogger(OracleSqlHelper.class);

    private LobHandler lobHandler;

    public void setClobValue(PreparedStatement ps, int index, String value) throws SQLException {
        this.lobHandler.getLobCreator().setClobAsString(ps, index, value);
    }

    public String getClobAsString(ResultSet rs, int index)
            throws SQLException {
        return this.lobHandler.getClobAsString(rs, index);
    }

    public void setLobHandler(LobHandler lobHandler) {
        this.lobHandler = lobHandler;
    }

    @Override
    public String getPageSql(String sql, int start, int num) {
        if (start < 0 || num < 0)
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

    @Override
    public String existTableSQL(String tableName, DataSource dataSource) throws Exception {
        String sql = "SELECT COUNT(TABLE_NAME) FROM USER_TABLES WHERE TABLE_NAME = '" + tableName.toUpperCase() + "'";

        return sql;
    }
}

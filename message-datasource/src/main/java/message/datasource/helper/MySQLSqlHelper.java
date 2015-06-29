package message.datasource.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * MySQL 辅助类
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2012-4-11 上午10:48:58
 */
public class MySQLSqlHelper extends AbstractSqlHelper {
    private static final Logger logger = LoggerFactory.getLogger(MySQLSqlHelper.class);

    @Override
    public String getPageSql(String sql, int start, int num) {
        if (start < 0 || num < 0)
            return sql;

        StringBuffer result = new StringBuffer();
        result.append(sql);
        result.append(" limit ").append(num);
        result.append(" offset ").append(start);

        return result.toString();
    }

    @Override
    public String existTableSQL(String tableName, DataSource dataSource) throws Exception {
        String name = dataSource.getConnection().getCatalog();

        String sql = "SELECT count(TABLE_NAME) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='" + name
                + "' and TABLE_NAME = '" + tableName + "'";

        return sql;
    }
}

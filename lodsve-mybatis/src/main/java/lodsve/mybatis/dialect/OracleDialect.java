package lodsve.mybatis.dialect;

/**
 * oracle.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-2-18 15:49
 */
public class OracleDialect extends AbstractDialect {
    @Override
    public String getPageSql(String sql, int offset, int limit) {
        if (offset < 0 || limit < 0)
            return sql;

        StringBuffer pageSql = new StringBuffer(" SELECT * FROM ( ");
        pageSql.append(" SELECT temp.* ,ROWNUM num FROM ( ");
        pageSql.append(sql);
        int last = offset + limit;
        pageSql.append(" ) temp where ROWNUM <= ").append(last);
        pageSql.append(" ) WHERE num > ").append(offset);

        return pageSql.toString();
    }

    @Override
    String existTableSql(String schema, String tableName) {
        return "SELECT COUNT(TABLE_NAME) FROM USER_TABLES WHERE TABLE_NAME = '" + tableName.toUpperCase() + "'";
    }
}

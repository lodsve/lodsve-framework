package message.mybatis.dialect;

/**
 * mysql数据库方言.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-2-18 15:42
 */
public class MySQLDialect extends AbstractDialect {
    @Override
    public String getPageSql(String sql, int offset, int limit) {
        StringBuilder sqlBuilder = new StringBuilder(sql);

        if (offset <= 0) {
            return sqlBuilder.append(" limit ").append(limit).toString();
        }
        return sqlBuilder.append(" limit ").append(offset).append(",").append(limit).toString();
    }

    @Override
    String existTableSql(String schema, String tableName) {
        StringBuilder sql = new StringBuilder("SELECT count(TABLE_NAME) count FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='");
        sql.append(schema).append("' and TABLE_NAME = '").append(tableName).append("'");

        return sql.toString();
    }
}

package message.mybatis.dialect;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 公用部分.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-2-18 15:52
 */
public abstract class AbstractDialect implements Dialect {
    @Override
    public String getCountSql(String sql) {
        StringBuilder sqlBuilder = new StringBuilder();
        return sqlBuilder.append("select count(*) from (").append(sql).append(") tmp_count").toString();
    }

    @Override
    public boolean existTable(String tableName, DataSource dataSource) throws Exception {
        Assert.notNull(dataSource);
        Assert.hasText(tableName);

        String name = dataSource.getConnection().getCatalog();
        PreparedStatement ps = DataSourceUtils.doGetConnection(dataSource).prepareStatement(existTableSql(name, tableName));
        ResultSet resultSet = ps.executeQuery();

        return resultSet.getInt("count") > 0;
    }

    /**
     * 判断表是否存在的sql
     *
     * @param schema
     * @param tableName
     * @return
     */
    abstract String existTableSql(String schema, String tableName);
}

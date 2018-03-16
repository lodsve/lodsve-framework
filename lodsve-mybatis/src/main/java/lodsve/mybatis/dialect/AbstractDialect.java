/*
 * Copyright (C) 2018  Sun.Hao
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.mybatis.dialect;

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
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            ps = DataSourceUtils.doGetConnection(dataSource).prepareStatement(existTableSql(name, tableName));
            resultSet = ps.executeQuery();

            return resultSet.next() && resultSet.getInt("count") > 0;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
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

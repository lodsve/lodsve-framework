/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
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

import lodsve.mybatis.query.NativeSqlQuery;
import lodsve.mybatis.utils.SqlUtils;
import org.springframework.util.Assert;

import javax.sql.DataSource;

/**
 * 公用部分.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016-2-18 15:52
 */
public abstract class AbstractDialect implements Dialect {
    @Override
    public String getCountSql(String sql) {
        return SqlUtils.getSingleLineCountSql(sql);
    }

    @Override
    public boolean existTable(String tableName, DataSource dataSource) throws Exception {
        Assert.notNull(dataSource, "dataSource must be non-null!");
        Assert.hasText(tableName, "dataSource must be non-null!");

        try (NativeSqlQuery query = new NativeSqlQuery(dataSource)) {
            return 0 < query.queryForInt(existTableSql(tableName, tableName));
        }
    }

    /**
     * 判断表是否存在的sql
     *
     * @param schema    schema
     * @param tableName table Name
     * @return 判断表是否存在的sql
     */
    abstract String existTableSql(String schema, String tableName);
}

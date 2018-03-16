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

/**
 * oracle.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-2-18 15:49
 */
public class OracleDialect extends AbstractDialect {
    @Override
    public String getPageSql(String sql, int offset, int limit) {
        if (offset < 0 || limit < 0) {
            return sql;
        }

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

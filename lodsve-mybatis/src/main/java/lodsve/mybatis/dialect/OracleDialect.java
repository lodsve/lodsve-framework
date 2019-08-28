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

/**
 * oracle.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016-2-18 15:49
 */
public class OracleDialect extends AbstractDialect {
    @Override
    public String getPageSql(String sql, int offset, int limit) {
        if (offset < 0 || limit < 0) {
            return sql;
        }

        int last = offset + limit;
        String pageSql = "SELECT * FROM " +
                "(SELECT temp.* ,ROWNUM num FROM (%s) temp where ROWNUM <= %d) " +
                "WHERE num > %d";

        return String.format(pageSql, sql, last, offset);
    }

    @Override
    String existTableSql(String schema, String tableName) {
        return "SELECT COUNT(TABLE_NAME) FROM USER_TABLES WHERE TABLE_NAME = '" + tableName.toUpperCase() + "'";
    }
}

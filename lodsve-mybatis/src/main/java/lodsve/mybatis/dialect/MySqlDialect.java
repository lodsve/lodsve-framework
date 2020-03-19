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
 * mysql数据库方言.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016-2-18 15:42
 */
public class MySqlDialect extends AbstractDialect {
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
        String sql = "SELECT count(TABLE_NAME) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='%s' and TABLE_NAME = '%s'";
        return String.format(sql, schema, tableName);
    }
}

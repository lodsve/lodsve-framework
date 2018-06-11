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

package lodsve.mybatis.utils;

import lodsve.mybatis.utils.format.SqlFormatter;

/**
 * sql utils.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017/6/12 下午8:27
 */
public final class SqlUtils {
    private final static SqlFormatter SQL_FORMATTER = new SqlFormatter();

    /**
     * 格式sql
     *
     * @param boundSql 原sql
     * @param format   是否格式化
     * @return 格式化后的sql
     */
    public static String sqlFormat(String boundSql, boolean format) {
        if (format) {
            return SQL_FORMATTER.format(boundSql);
        } else {
            return boundSql.replaceAll("[\\s]+", " ");
        }
    }
}

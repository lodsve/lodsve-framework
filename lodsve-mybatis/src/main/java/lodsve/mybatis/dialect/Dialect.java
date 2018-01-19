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

import javax.sql.DataSource;

/**
 * 数据库方言.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-2-18 15:40
 */
public interface Dialect {
    /**
     * 获得查询所有条数的sql
     *
     * @param sql 原sql
     * @return
     */
    String getCountSql(String sql);

    /**
     * 获得分页的sql
     * 由于各个数据库分页语句不同，故让子类自己实现此方法
     *
     * @param sql    原sql
     * @param offset 偏移量
     * @param limit  数量
     * @return
     */
    String getPageSql(String sql, int offset, int limit);

    /**
     * 判断数据库中是否含有给定的表
     *
     * @param tableName  表名
     * @param dataSource 数据源
     * @return
     * @throws Exception
     */
    boolean existTable(String tableName, DataSource dataSource) throws Exception;
}

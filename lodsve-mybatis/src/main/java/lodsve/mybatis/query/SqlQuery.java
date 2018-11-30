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

package lodsve.mybatis.query;

import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * sql query.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public abstract class SqlQuery implements AutoCloseable {
    final Connection connection;
    private final DataSource dataSource;

    public SqlQuery(DataSource dataSource) {
        this.connection = DataSourceUtils.getConnection(dataSource);
        this.dataSource = dataSource;
    }

    @Override
    public void close() throws Exception {
        closeResources();

        DataSourceUtils.releaseConnection(connection, dataSource);
    }

    /**
     * 关闭其他资源
     *
     * @throws SQLException SQLException
     */
    public abstract void closeResources() throws SQLException;
}

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

import lodsve.core.utils.StringUtils;
import lodsve.mybatis.dialect.Dialect;
import lodsve.mybatis.dialect.MySQLDialect;
import lodsve.mybatis.dialect.OracleDialect;
import lodsve.mybatis.enums.DbType;
import lodsve.mybatis.exception.MyBatisException;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MyBatis utils.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016-2-18 16:03
 */
public final class MyBatisUtils {
    private MyBatisUtils() {
    }

    public static DbType getDbType(DataSource dataSource) {
        String database;
        try (Connection connection = dataSource.getConnection()) {
            database = connection.getMetaData().getDatabaseProductName();
        } catch (SQLException e) {
            throw new MyBatisException("can't find DbType!");
        }

        if (StringUtils.equalsIgnoreCase(database, DbType.DB_MYSQL.getName())) {
            return DbType.DB_MYSQL;
        } else if (StringUtils.equalsIgnoreCase(database, DbType.DB_ORACLE.getName())) {
            return DbType.DB_ORACLE;
        }

        throw new MyBatisException(102004, "can't find DbType!", database);
    }

    public static Dialect getDialect(Connection connection) throws SQLException {
        String database = connection.getMetaData().getDatabaseProductName();

        if (StringUtils.equalsIgnoreCase(database, DbType.DB_MYSQL.getName())) {
            return new MySQLDialect();
        } else if (StringUtils.equalsIgnoreCase(database, DbType.DB_ORACLE.getName())) {
            return new OracleDialect();
        }

        throw new MyBatisException(102001, "can't find dialect!", database);
    }

    public static int queryForInt(DataSource dataSource, String sql, Object... params) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = DataSourceUtils.doGetConnection(dataSource).prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return -1;
            }
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public static int executeSql(DataSource dataSource, String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = DataSourceUtils.doGetConnection(dataSource).prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            return ps.executeUpdate();
        }
    }
}

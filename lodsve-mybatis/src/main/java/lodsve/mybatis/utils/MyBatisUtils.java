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
import lodsve.mybatis.dialect.MySqlDialect;
import lodsve.mybatis.dialect.OracleDialect;
import lodsve.mybatis.exception.MyBatisException;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;
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
    public static Method method;

    static {
        try {
            Class<?> metaClass = Class.forName("org.apache.ibatis.reflection.SystemMetaObject");
            method = metaClass.getDeclaredMethod("forObject", Object.class);
        } catch (Exception e1) {
            try {
                Class<?> metaClass = Class.forName("org.apache.ibatis.reflection.MetaObject");
                method = metaClass.getDeclaredMethod("forObject", Object.class);
            } catch (Exception e2) {
                throw new MyBatisException(e2.getMessage());
            }
        }

    }

    private MyBatisUtils() {
    }

    public static DbType getDbType(DataSource dataSource) {
        String database;
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            database = connection.getMetaData().getDatabaseProductName();
        } catch (SQLException e) {
            throw new MyBatisException("can't find DbType!");
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
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
            return new MySqlDialect();
        } else if (StringUtils.equalsIgnoreCase(database, DbType.DB_ORACLE.getName())) {
            return new OracleDialect();
        }

        throw new MyBatisException(102001, "can't find dialect!", database);
    }

    public static int queryForInt(DataSource dataSource, String sql, Object... params) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DataSourceUtils.getConnection(dataSource);
            ps = connection.prepareStatement(sql);
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
            releaseResource(rs, ps, connection, dataSource);
        }
    }

    public static void releaseResource(ResultSet rs, PreparedStatement ps, Connection connection, DataSource dataSource) throws SQLException {
        if (rs != null) {
            rs.close();
        }
        if (ps != null) {
            ps.close();
        }
        if (connection != null) {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    public static MetaObject forObject(Object object) {
        try {
            return (MetaObject) method.invoke(null, object);
        } catch (Exception e) {
            throw new MyBatisException(e.getMessage());
        }
    }
}

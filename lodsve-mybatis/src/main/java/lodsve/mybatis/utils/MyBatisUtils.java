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
import lodsve.mybatis.dialect.*;
import lodsve.mybatis.exception.MyBatisException;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new MyBatisException(e.getMessage());
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
        } else if (StringUtils.equalsIgnoreCase(database, DbType.DB_HSQL.getName())) {
            return new HsqlDialect();
        } else if (StringUtils.equalsIgnoreCase(database, DbType.DB_SQL_SERVER.getName())) {
            return new SqlServerDialect();
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
        return SystemMetaObject.forObject(object);
    }

    /**
     * <p>Recursive get the original target object.
     * <p>If integrate more than a plugin, maybe there are conflict in these plugins, because plugin will proxy the object.<br>
     * So, here get the orignal target object
     *
     * @param target proxy-object
     * @return original target object
     */
    public static Object processTarget(Object target) {
        if (Proxy.isProxyClass(target.getClass())) {
            MetaObject mo = SystemMetaObject.forObject(target);
            return processTarget(mo.getValue("h.target"));
        }

        return target;
    }

    /**
     * 获取连接，如果使用了事务，从事务管理器中获取
     *
     * @param configuration mybatis configuration
     * @return 连接
     */
    public static Connection getConnection(Configuration configuration) {
        Assert.notNull(configuration, "No Configuration specified");

        DataSource dataSource = configuration.getEnvironment().getDataSource();
        return DataSourceUtils.getConnection(dataSource);
    }

    /**
     * 释放连接，代替直接close连接，否则事务会有问题，这里的连接是用事务管理器中拿到的，如果直接关闭连接，接下来的事务就会没有连接用了，会报错！
     *
     * @param connection    连接
     * @param configuration mybatis configuration
     */
    public static void releaseConnection(Connection connection, Configuration configuration) {
        Assert.notNull(connection, "No Connection specified");

        DataSource dataSource = configuration.getEnvironment().getDataSource();
        DataSourceUtils.releaseConnection(connection, dataSource);
    }
}

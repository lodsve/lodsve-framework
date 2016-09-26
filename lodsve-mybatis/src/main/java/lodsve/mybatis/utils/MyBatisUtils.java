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
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-2-18 16:03
 */
public final class MyBatisUtils {
    private MyBatisUtils() {
    }

    public static DbType getDbType(Connection connection) throws SQLException {
        String database = connection.getMetaData().getDatabaseProductName();

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
        PreparedStatement ps = null;
        try {
            ps = DataSourceUtils.doGetConnection(dataSource).prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            return ps.executeUpdate();
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }
}

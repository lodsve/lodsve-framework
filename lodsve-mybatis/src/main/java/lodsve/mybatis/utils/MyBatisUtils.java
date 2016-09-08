package lodsve.mybatis.utils;

import lodsve.core.utils.StringUtils;
import lodsve.mybatis.dialect.Dialect;
import lodsve.mybatis.dialect.MySQLDialect;
import lodsve.mybatis.dialect.OracleDialect;
import lodsve.mybatis.enums.DbType;
import lodsve.mybatis.exception.MyBatisException;
import lodsve.mybatis.key.IDGenerator;
import lodsve.mybatis.key.snowflake.SnowflakeIdGenerator;
import lodsve.mybatis.key.uuid.UUIDGenerator;

import java.sql.Connection;
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

    public static Dialect getDialect(Connection connection) throws SQLException {
        String database = connection.getMetaData().getDatabaseProductName();

        if (StringUtils.equalsIgnoreCase(database, DbType.DB_MYSQL.getName())) {
            return new MySQLDialect();
        } else if (StringUtils.equalsIgnoreCase(database, DbType.DB_ORACLE.getName())) {
            return new OracleDialect();
        }

        throw new MyBatisException(102001, "can't find dialect!", database);
    }

    public static IDGenerator getIDGenerator(IDGenerator.KeyType keyType) {
        if (IDGenerator.KeyType.SNOWFLAKE == keyType) {
            return new SnowflakeIdGenerator();
        } else if (IDGenerator.KeyType.UUID == keyType) {
            return new UUIDGenerator();
        }

        throw new MyBatisException(102002, "can't find IDGenerator!", keyType.toString());
    }

    public static <T> T nextId(IDGenerator.KeyType keyType) {
        return (T) getIDGenerator(keyType).nextId();
    }
}

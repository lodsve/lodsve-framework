package lodsve.mybatis.utils;

import lodsve.mybatis.dialect.Dialect;
import lodsve.mybatis.dialect.MySQLDialect;
import lodsve.mybatis.dialect.OracleDialect;
import lodsve.mybatis.key.IDGenerator;
import lodsve.mybatis.key.snowflake.SnowflakeIdGenerator;
import lodsve.mybatis.key.uuid.UUIDGenerator;
import org.springframework.util.ClassUtils;

/**
 * MyBatis utils.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-2-18 16:03
 */
public final class MyBatisUtils {
    private MyBatisUtils() {
    }

    public static final boolean IS_MYSQL = ClassUtils.isPresent("com.mysql.jdbc.Driver", MyBatisUtils.class.getClassLoader());
    public static final boolean IS_ORACLE = ClassUtils.isPresent("oracle.jdbc.driver.OracleDriver", MyBatisUtils.class.getClassLoader());

    public static Dialect getDialect() {
        if (IS_MYSQL) {
            return new MySQLDialect();
        }

        if (IS_ORACLE) {
            return new OracleDialect();
        }

        throw new RuntimeException("找不到Dialect！");
    }

    public static IDGenerator getIDGenerator(IDGenerator.KeyType keyType) {
        if (IDGenerator.KeyType.SNOWFLAKE == keyType) {
            return new SnowflakeIdGenerator();
        } else if (IDGenerator.KeyType.UUID == keyType) {
            return new UUIDGenerator();
        }

        throw new RuntimeException("找不到IDGenerator！");
    }

    public static <T> T nextId(IDGenerator.KeyType keyType) {
        return (T) getIDGenerator(keyType).nextId();
    }
}

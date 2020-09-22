/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lodsve.mybatis.utils;

import lodsve.mybatis.dialect.*;
import lodsve.mybatis.exception.MyBatisException;
import lodsve.mybatis.query.NativeSqlQuery;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * MyBatis utils.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016-2-18 16:03
 */
public final class MyBatisUtils {
    private static final Logger logger = LoggerFactory.getLogger(MyBatisUtils.class);
    private static DbType dbType;
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

    /**
     * 设置方言，只能调用一次！
     *
     * @param dbType 数据库方言
     */
    public static void setDbType(DbType dbType) {
        if (null != MyBatisUtils.dbType) {
            // 只允许修改一次
            if (logger.isErrorEnabled()) {
                logger.error("dbType已存在！不允许再设置！");
            }
            return;
        }
        MyBatisUtils.dbType = dbType;
    }

    public static DbType getDbType() {
        if (null != dbType) {
            return dbType;
        }

        throw new MyBatisException(102004, "can't find DbType!");
    }

    public static Dialect getDialect() {
        DbType dbType = getDbType();

        switch (dbType) {
            case DB_HSQL:
                return new HsqlDialect();
            case DB_MYSQL:
                return new MySqlDialect();
            case DB_ORACLE:
                return new OracleDialect();
            case DB_SQL_SERVER:
                return new SqlServerDialect();
            default:
                return new MySqlDialect();
        }
    }

    public static int queryForInt(DataSource dataSource, String sql, Object... params) throws Exception {
        try (NativeSqlQuery query = new NativeSqlQuery(dataSource)) {
            return query.queryForInt(sql, params);
        }
    }

    private static MetaObject forObject(Object object) {
        return SystemMetaObject.forObject(object);
    }

    /**
     * 通过mybatis的MetaObject设置对象某个字段的值
     *
     * @param object    需要设置值得对象
     * @param fieldName 字段名
     * @param value     字段值
     */
    public static void setValue(Object object, String fieldName, Object value) {
        Assert.notNull(object, "object must be non-null!");

        MetaObject metaObject = MyBatisUtils.forObject(object);
        metaObject.setValue(fieldName, value);
    }

    /**
     * 通过mybatis的MetaObject获取对象某个字段的值
     *
     * @param object    需要获取值得对象
     * @param fieldName 字段名
     * @param <T>       值的类型
     * @return 某个字段的值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValue(Object object, String fieldName) {
        Assert.notNull(object, "object must be non-null!");

        MetaObject metaObject = MyBatisUtils.forObject(object);
        return (T) metaObject.getValue(fieldName);
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
            return processTarget(getValue(target, "h.target"));
        }

        return target;
    }
}

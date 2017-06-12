package lodsve.mybatis.utils;

import lodsve.mybatis.utils.format.SqlFormatter;

/**
 * sql utils.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/6/12 下午8:27
 */
public final class SqlUtils {
    private final static SqlFormatter sqlFormatter = new SqlFormatter();

    /**
     * 格式sql
     *
     * @param boundSql 原sql
     * @param format   是否格式化
     * @return 格式化后的sql
     */
    public static String sqlFormat(String boundSql, boolean format) {
        if (format) {
            return sqlFormatter.format(boundSql);
        } else {
            return boundSql.replaceAll("[\\s]+", " ");
        }
    }
}

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
     */
    boolean existTable(String tableName, DataSource dataSource) throws Exception;
}

package message.datasource.helper;

import message.datasource.key.IDGenerator;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 不同数据库处理的辅助类
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2012-4-10 上午01:23:20
 */
public interface SqlHelper {
    /**
     * 设置clob型值
     *
     * @param ps
     * @param index
     * @param value
     * @throws SQLException
     */
    void setClobValue(PreparedStatement ps, int index, String value) throws SQLException;

    /**
     * 获取long型值，并返回String
     *
     * @param rs
     * @param index
     * @return
     * @throws SQLException
     */
    String getLongAsString(ResultSet rs, int index) throws SQLException;

    /**
     * 获取clob型值
     *
     * @param rs
     * @param index
     * @return
     * @throws SQLException
     */
    String getClobAsString(ResultSet rs, int index) throws SQLException, IOException;

    /**
     * 获得查询所有条数的sql
     *
     * @param sql
     * @return
     */
    String getCountSql(String sql);

    void setIdGenerator(IDGenerator idGenerator);

    /**
     * 获得分页的sql
     * 由于各个数据库分页语句不同，故让子类自己实现此方法
     *
     * @param sql
     * @param start
     * @param num
     * @return
     */
    String getPageSql(String sql, int start, int num);

    /**
     * 通过sequence获取下一个主键的值<br/>
     * oracle:sequence<br/>
     * mysql：模拟的sequence表的表名
     *
     * @param sequenceName
     * @return
     */
    Long getNextId(String sequenceName);

    /**
     * 判断数据库中是否含有给定的表
     *
     * @param tableName  表名
     * @param dataSource 数据源
     * @return
     */
    String existTableSQL(String tableName, DataSource dataSource) throws Exception;
}

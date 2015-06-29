package message.datasource.helper;

import message.base.convert.ConvertGetter;
import message.datasource.convert.Convert;
import message.datasource.key.IDGenerator;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

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
    public abstract void setClobValue(PreparedStatement ps, int index, String value) throws SQLException;

    /**
     * 获取long型值，并返回String
     *
     * @param rs
     * @param index
     * @return
     * @throws SQLException
     */
    public String getLongAsString(ResultSet rs, int index) throws SQLException;

    /**
     * 获取clob型值
     *
     * @param rs
     * @param index
     * @return
     * @throws SQLException
     */
    public String getClobAsString(ResultSet rs, int index) throws SQLException, IOException;

    /**
     * 获得查询所有条数的sql
     *
     * @param sql
     * @return
     */
    public String getCountSql(String sql);

    public void setIdGenerator(IDGenerator idGenerator);

    /**
     * 获得分页的sql
     * 由于各个数据库分页语句不同，故让子类自己实现此方法
     *
     * @param sql
     * @param start
     * @param num
     * @return
     */
    public String getPageSql(String sql, int start, int num);

    /**
     * 通过sequence获取下一个主键的值<br/>
     * oracle:sequence<br/>
     * mysql：模拟的sequence表的表名
     *
     * @param sequenceName
     * @return
     */
    public Long getNextId(String sequenceName);

    /**
     * 判断数据库中是否含有给定的表
     *
     * @param tableName  表名
     * @param dataSource 数据源
     * @return
     */
    public String existTableSQL(String tableName, DataSource dataSource) throws Exception;

    /**
     * 设置类型转换
     *
     * @param convertBeans
     */
    public void setConvertBeans(Map<Class<?>, Class<?>> convertBeans);

    /**
     * 获取类型转换
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends ConvertGetter> Convert<T> getConvert(Class<T> clazz);
}

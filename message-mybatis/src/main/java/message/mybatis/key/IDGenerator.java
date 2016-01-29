package message.mybatis.key;

import org.springframework.dao.DataAccessException;

import javax.sql.DataSource;

/**
 * make database id
 * oracle:sequence	mysql:auto
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2012-4-11 上午08:10:32
 */
public interface IDGenerator {
    String SNOWFLAKE = "snowflake";

    /**
     * get next long value
     *
     * @param name
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public long nextLongValue(String name) throws DataAccessException;

    /**
     * set datasource
     *
     * @param dataSource
     */
    public void setDataSource(DataSource dataSource);
}

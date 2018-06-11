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

package lodsve.mybatis.key.mysql;

import lodsve.mybatis.key.IDGenerator;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * mysql主键.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/9/14 上午9:57
 */
public class MySQLIDGenerator implements IDGenerator {
    /**
     * The SQL string for retrieving the new sequence value
     */
    private static final String VALUE_SQL = "select last_insert_id()";

    /**
     * The next id to serve
     */
    private long nextId = 0;

    /**
     * The max id to serve
     */
    private long maxId = 0;

    /**
     * The number of keys buffered in a cache
     */
    private int cacheSize = 10;

    /**
     * cache for each sequence's next id
     **/
    private final static Map<String, Long> NEXT_ID_CACHE = new HashMap<>();

    /**
     * cache for each sequence's max id
     **/
    private final static Map<String, Long> MAX_ID_CACHE = new HashMap<>();

    private DataSource dataSource;

    @Override
    public synchronized Long nextId(String sequenceName) {
        if (NEXT_ID_CACHE.get(sequenceName) != null) {
            this.nextId = NEXT_ID_CACHE.get(sequenceName);
        }
        if (MAX_ID_CACHE.get(sequenceName) != null) {
            this.maxId = MAX_ID_CACHE.get(sequenceName);
        }

        if (this.maxId == this.nextId) {
            /*
             * Need to use straight JDBC code because we need to make sure that the insert and select
             * are performed on the same connection (otherwise we can't be sure that last_insert_id()
             * returned the correct value)
             */

            Connection con = DataSourceUtils.getConnection(dataSource);
            Statement stmt = null;
            try {
                stmt = con.createStatement();
                DataSourceUtils.applyTransactionTimeout(stmt, dataSource);
                String updateSql = "update " + sequenceName + " set " + sequenceName + " = last_insert_id(" + sequenceName + " + " + getCacheSize() + ")";
                try {
                    // Increment the sequence column...
                    stmt.executeUpdate(updateSql);
                } catch (SQLException e) {
                    //发生异常,即不存在这张表
                    //1.执行新建这张表的语句
                    StringBuffer createSql = new StringBuffer();
                    createSql.append("create table ").append(sequenceName).append(" ( ");
                    createSql.append(sequenceName).append(" bigint(12) default null ");
                    createSql.append(" ) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ");
                    stmt.execute(createSql.toString());
                    //2.初始化这张表的数据(将name的值置为0)
                    String initDataSql = "insert into " + sequenceName + " values(0)";
                    stmt.executeUpdate(initDataSql);
                    //3.Increment the sequence column...
                    stmt.executeUpdate(updateSql);
                }
                // Retrieve the new max of the sequence column...
                ResultSet rs = stmt.executeQuery(VALUE_SQL);
                try {
                    if (!rs.next()) {
                        throw new DataAccessResourceFailureException("last_insert_id() failed after executing an update");
                    }
                    this.maxId = rs.getLong(1);
                    //更新缓存
                    MAX_ID_CACHE.put(sequenceName, this.maxId);
                } finally {
                    JdbcUtils.closeResultSet(rs);
                }
                this.nextId = this.maxId - getCacheSize() + 1;
                //更新缓存
                NEXT_ID_CACHE.put(sequenceName, this.nextId);
            } catch (SQLException ex) {
                throw new DataAccessResourceFailureException("Could not obtain last_insert_id()", ex);
            } finally {
                JdbcUtils.closeStatement(stmt);
                DataSourceUtils.releaseConnection(con, dataSource);
            }
        } else {
            this.nextId++;
            //更新缓存
            NEXT_ID_CACHE.put(sequenceName, this.nextId);
        }
        long result = this.nextId;

        //初始化nextId和maxId
        this.nextId = 0;
        this.maxId = 0;
        return result;
    }

    /**
     * Return the number of buffered keys.
     */
    public int getCacheSize() {
        return cacheSize;
    }

    /**
     * Set the number of buffered keys.
     */
    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}

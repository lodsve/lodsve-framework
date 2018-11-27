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

import lodsve.mybatis.dialect.Dialect;
import lodsve.mybatis.exception.MyBatisException;
import lodsve.mybatis.key.IDGenerator;
import lodsve.mybatis.utils.MyBatisUtils;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * mysql主键.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-07-09 13:50
 */
public class MySQLIDGenerator implements IDGenerator {

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

    private static final String SEQUENCE_TABLE = "t_sequence";
    private static final String CREATE_SEQUENCE_TABLE = "CREATE TABLE t_sequence (name varchar(32) NOT NULL,value bigint(5) NOT NULL,PRIMARY KEY (name))";
    private static final String GET_NEXT_ID_SQL = "SELECT value FROM t_sequence WHERE name = ?";
    private static final String UPDATE_NEXT_ID_SQL = "UPDATE t_sequence SET value = ? WHERE name = ?";
    private static final String INSERT_NEXT_ID_SQL = "INSERT INTO t_sequence (value, name) VALUES (?, ?)";

    @PostConstruct
    private void init() {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        Dialect dialect;
        try {
            dialect = MyBatisUtils.getDialect(connection);
        } catch (SQLException e) {
            throw new MyBatisException(102001, String.format("校验表%s是否存在发生异常,异常信息%s,请检查!", CREATE_SEQUENCE_TABLE, e.getMessage()));
        }

        if (isExistTable(dialect)) {
            return;
        }

        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            DataSourceUtils.applyTransactionTimeout(stmt, dataSource);
            stmt.executeUpdate(CREATE_SEQUENCE_TABLE);
        } catch (SQLException e) {
            throw new MyBatisException(102001, String.format("创建表%s失败!,异常信息%s,请检查!", SEQUENCE_TABLE, e.getMessage()));
        } finally {
            if (stmt != null) {
                JdbcUtils.closeStatement(stmt);
            }
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public Long nextId(String sequenceName) {
        // 可重入互斥锁
        Lock lock = new ReentrantLock();
        lock.lock();

        try {
            return getNextIdWithLock(sequenceName);
        } finally {
            lock.unlock();
        }
    }

    private Long getNextIdWithLock(String sequenceName) {
        Assert.hasText(sequenceName);

        // 缓存中的
        Long nextId = NEXT_ID_CACHE.get(sequenceName);
        Long maxId = MAX_ID_CACHE.get(sequenceName);

        if (nextId != null && nextId <= maxId) {
            NEXT_ID_CACHE.put(sequenceName, nextId + 1);
            return nextId;
        }

        return findNextIdFromDatabase(sequenceName);
    }

    private Long findNextIdFromDatabase(String sequenceName) {
        long nextId;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement(GET_NEXT_ID_SQL);
            stmt.setString(1, sequenceName);
            rs = stmt.executeQuery();

            String sql;
            if (rs.next()) {
                nextId = rs.getLong("value");
                sql = UPDATE_NEXT_ID_SQL;
            } else {
                nextId = 1L;
                sql = INSERT_NEXT_ID_SQL;
            }
            stmt = connection.prepareStatement(sql);
            stmt.setLong(1, nextId + cacheSize + 1);
            stmt.setString(2, sequenceName);
            stmt.execute();

            // 缓存最大值
            MAX_ID_CACHE.put(sequenceName, nextId + cacheSize);
            NEXT_ID_CACHE.put(sequenceName, nextId + 1);

            return nextId;
        } catch (SQLException e) {
            throw new MyBatisException(e.getMessage());
        } finally {
            if (rs != null) {
                JdbcUtils.closeResultSet(rs);
            }
            if (stmt != null) {
                JdbcUtils.closeStatement(stmt);
            }
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    private boolean isExistTable(Dialect dialect) {
        try {
            return dialect.existTable(SEQUENCE_TABLE, dataSource);
        } catch (Exception e) {
            return false;
        }
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}

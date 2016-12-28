package lodsve.mybatis.key.mysql;

import lodsve.mybatis.exception.MyBatisException;
import lodsve.mybatis.key.IDGenerator;
import lodsve.mybatis.utils.MyBatisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * mysql主键.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 16/9/14 上午9:57
 */
public class MySQLIDGenerator implements IDGenerator {
    private static final Logger logger = LoggerFactory.getLogger(MySQLIDGenerator.class);

    private DataSource dataSource;

    private static final String SEQUENCE_TABLE = "t_sequence";

    private static final String CREATE_SEQUENCE_TABLE = "CREATE TABLE t_sequence (name varchar(32) NOT NULL,value bigint(5) NOT NULL,PRIMARY KEY (name))";
    private static final String GET_NEXT_ID_SQL = "SELECT value FROM t_sequence WHERE name = ?";
    private static final String UPDATE_NEXT_ID_SQL = "UPDATE t_sequence SET value = ? WHERE name = ?";
    private static final String INSERT_NEXT_ID_SQL = "INSERT INTO t_sequence (value, name) VALUES (?, ?)";

    /**
     * cache for each sequence's next id
     **/
    private static final Map<String, Long> NEXT_ID_CACHE = new HashMap<>();
    /**
     * cache for each sequence's max id
     **/
    private static final Map<String, Long> MAX_ID_CACHE = new HashMap<>();
    /**
     * The number of keys buffered in a cache
     */
    private static final int CACHE_SIZE = 20;

    public MySQLIDGenerator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void init() throws SQLException {
        if (isExistTable()) {
            return;
        }


        // 初始化这张表
        try {
            MyBatisUtils.executeSql(dataSource, CREATE_SEQUENCE_TABLE);
        } catch (SQLException e) {
            logger.error(String.format("执行sql语句[%s]发送错误,请检查!", CREATE_SEQUENCE_TABLE), e);
        }
    }

    @Override
    public synchronized Long nextId(String sequenceName) {
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

    private boolean isExistTable() {
        try {
            return MyBatisUtils.getDialect(dataSource.getConnection()).existTable(SEQUENCE_TABLE, dataSource);
        } catch (Exception e) {
            return false;
        }
    }

    private Long findNextIdFromDatabase(String sequenceName) {
        try {
            Long nextId = (long) MyBatisUtils.queryForInt(dataSource, GET_NEXT_ID_SQL, sequenceName);
            String sql;
            if (!Long.valueOf(-1).equals(nextId)) {
                sql = UPDATE_NEXT_ID_SQL;
            } else {
                nextId = 1L;
                sql = INSERT_NEXT_ID_SQL;
            }

            // 更新数据库中的值 nextId+CACHE_SIZE+1
            MyBatisUtils.executeSql(dataSource, sql, nextId + CACHE_SIZE + 1, sequenceName);

            // 缓存最大值
            MAX_ID_CACHE.put(sequenceName, nextId + CACHE_SIZE);
            NEXT_ID_CACHE.put(sequenceName, nextId + 1);

            return nextId;
        } catch (SQLException e) {
            throw new MyBatisException(102003, e.getMessage());
        }
    }
}

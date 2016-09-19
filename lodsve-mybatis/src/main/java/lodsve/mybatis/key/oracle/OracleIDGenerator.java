package lodsve.mybatis.key.oracle;

import lodsve.mybatis.exception.MyBatisException;
import lodsve.mybatis.key.IDGenerator;
import lodsve.mybatis.utils.MyBatisUtils;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * oracle主键.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 16/9/14 上午9:57
 */
public class OracleIDGenerator implements IDGenerator {
    private DataSource dataSource;

    public OracleIDGenerator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Long nextId(String sequenceName) {
        Assert.hasText(sequenceName);

        String sql = String.format("SELECT %s.NEXTVAL FROM DUAL", sequenceName);

        try {
            return (long) MyBatisUtils.queryForInt(dataSource, sql);
        } catch (SQLException e) {
            throw new MyBatisException(102003, e.getMessage());
        }
    }
}

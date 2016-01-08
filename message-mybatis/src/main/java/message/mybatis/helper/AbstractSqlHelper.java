package message.mybatis.helper;

import message.mybatis.key.IDGenerator;
import message.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialClob;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 共有方法.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/6/20 上午12:38
 */
public abstract class AbstractSqlHelper implements SqlHelper {
    private static final Logger logger = LoggerFactory.getLogger(AbstractSqlHelper.class);
    protected IDGenerator idGenerator;

    @Override
    public void setClobValue(PreparedStatement ps, int index, String value) throws SQLException {
        ps.setClob(index, new SerialClob(value.toCharArray()));
    }

    @Override
    public String getLongAsString(ResultSet rs, int index) throws SQLException {
        return rs.getString(index);
    }

    @Override
    public String getClobAsString(ResultSet rs, int index) throws SQLException, IOException {
        Clob clob = rs.getClob(index);

        Reader is = clob.getCharacterStream();// 得到流
        BufferedReader br = new BufferedReader(is);
        String s = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (s != null) {// 执行循环将字符串全部取出付值给 StringBuffer由StringBuffer转成STRING
            sb.append(s);
            s = br.readLine();
        }
        return sb.toString();
    }

    @Override
    public String getCountSql(String sql) {
        StringBuffer countSql = new StringBuffer("select count(*) from (");
        countSql.append(sql).append(") total");

        return countSql.toString();
    }

    @Override
    public void setIdGenerator(IDGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public abstract String getPageSql(String sql, int start, int num);

    @Override
    public Long getNextId(String sequenceName) {
        if (StringUtils.isEmpty(sequenceName)) {
            logger.warn("given null sequenceName!");
            return null;
        }

        return idGenerator.nextLongValue(sequenceName);
    }

    @Override
    public abstract String existTableSQL(String tableName, DataSource dataSource) throws Exception;
}

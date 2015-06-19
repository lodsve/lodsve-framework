package message.jdbc.type;

import message.jdbc.helper.SqlHelper;
import org.springframework.jdbc.core.SqlTypeValue;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 12-6-17 下午8:44
 */
public abstract class AbstractStringSqlTypeValue implements SqlTypeValue {
    protected String value;
    protected SqlHelper sqlHelper;

    protected AbstractStringSqlTypeValue(SqlHelper sqlHelper, String value) {
        this.value = value;
        this.sqlHelper = sqlHelper;
    }

    public abstract void setTypeValue(PreparedStatement ps, int paramIndex, int sqlType, String typeName) throws SQLException;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SqlHelper getSqlHelper() {
        return sqlHelper;
    }

    public void setSqlHelper(SqlHelper sqlHelper) {
        this.sqlHelper = sqlHelper;
    }
}

package message.jdbc.type;

import message.jdbc.helper.SqlHelper;
import message.utils.NumberUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 12-6-17 下午8:44
 */
public class LongStringSqlTypeValue extends AbstractStringSqlTypeValue {

    public LongStringSqlTypeValue(SqlHelper sqlHelper, String value) {
        super(sqlHelper, value);
    }

    public void setTypeValue(PreparedStatement ps, int paramIndex, int sqlType, String typeName) throws SQLException {
        if(NumberUtils.isNumber(value)) {
            ps.setLong(paramIndex, Long.valueOf(value));
        } else {
            throw new SQLException("value [" + value + "] is not a number!");
        }
    }
}

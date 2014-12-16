package message.jdbc.ext;

import message.jdbc.convert.Convert;
import message.jdbc.convert.ConvertGetter;
import message.jdbc.utils.ClobStringSqlTypeValue;
import message.jdbc.utils.LongStringSqlTypeValue;
import message.jdbc.utils.ParamType;
import message.jdbc.utils.helper.SqlHelper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 12-6-17 下午8:04
 */
public class ExtMapSqlParameterSource extends AbstractSqlParameterSource {
    private final Map values = new HashMap();
    private SqlHelper sqlHelper;

    public ExtMapSqlParameterSource() {
    }

    public ExtMapSqlParameterSource(Map values, SqlHelper sqlHelper) {
        addValues(values);
        this.sqlHelper = sqlHelper;
    }

    public ExtMapSqlParameterSource addValues(Map values) {
        if (values != null) {
            Map newVlaues = new HashMap();
            for (Iterator it = values.keySet().iterator(); it.hasNext(); ) {
                Object key = it.next();
                Object value = values.get(key);

                Convert convert = this.sqlHelper.getConvert(key.getClass());
                if(convert != null) {
                    value = value != null ? convert.getDbValue((ConvertGetter) value) : convert.getDbNullValue((ConvertGetter) value);
                }

                if (key instanceof String) {
                    ParamType paramType = ParamType.getParamType((String) key);
                    newVlaues.put(paramType.getRealParamName(), value);
                } else {
                    newVlaues.put(key, value);
                }
            }
            this.values.putAll(newVlaues);
            for (Iterator it = values.keySet().iterator(); it.hasNext(); ) {
                Object key = it.next();
                Object value = values.get(key);
                if (value != null && key instanceof String && value instanceof SqlParameterValue) {
                    super.registerSqlType((String) key, ((SqlParameterValue) value).getSqlType());
                }
            }
        }
        return this;
    }

    public boolean hasValue(String paramName) {
        ParamType paramType = ParamType.getParamType(paramName);
        return this.values.containsKey(paramType.getRealParamName());
    }

    public Object getValue(String paramName) throws IllegalArgumentException {
        ParamType paramType = ParamType.getParamType(paramName);
        if (!this.values.containsKey(paramType.getRealParamName())) {
            throw new IllegalArgumentException("No value registered for key '" + paramName + "'");
        }

        Object obj = this.values.get(paramType.getRealParamName());
        if (paramType.getValueType() == ParamType.CLOB) {
            return new ClobStringSqlTypeValue(this.sqlHelper, (String) obj);
        } else if (paramType.getValueType() == ParamType.LONGSTRING) {
            return new LongStringSqlTypeValue(this.sqlHelper, (String) obj);
        }

        return obj;
    }

    public Map getValues() {
        return Collections.unmodifiableMap(this.values);
    }

    public void setSqlHelper(SqlHelper sqlHelper) {
        this.sqlHelper = sqlHelper;
    }

    public SqlHelper getSqlHelper() {
        return sqlHelper;
    }
}

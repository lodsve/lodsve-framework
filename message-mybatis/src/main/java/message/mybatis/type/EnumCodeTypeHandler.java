package message.mybatis.type;

import message.base.convert.ConvertGetter;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * mybatis对pojo对象中得枚举与数据库字段映射关系的处理.<br/>
 * 继承的类必须实现空得构造器,然后调用父类的含有clazz参数的构造器
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/6/25 下午7:29
 */
public abstract class EnumCodeTypeHandler<T extends Enum<?> & ConvertGetter> extends BaseTypeHandler<T> {
    private static final Logger logger = LoggerFactory.getLogger(EnumCodeTypeHandler.class);
    private T[] enums;

    public EnumCodeTypeHandler(Class<T> clazz){
        if(clazz == null) {
            throw new IllegalArgumentException("enum type can't be null!");
        }

        this.enums = clazz.getEnumConstants();
        if(this.enums == null) {
            logger.warn("This enum '{}' has none enum contents!", clazz.getSimpleName());
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return convert(rs.getString(columnName));
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return convert(rs.getString(columnIndex));
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convert(cs.getString(columnIndex));
    }

    private T convert(String value) {
        for (T em : this.enums) {
            if (em.getValue().equals(value)) {
                return em;
            }
        }
        return null;
    }
}

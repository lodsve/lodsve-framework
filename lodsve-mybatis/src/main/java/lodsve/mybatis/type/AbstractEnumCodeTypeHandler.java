/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lodsve.mybatis.type;

import lodsve.core.bean.Codeable;
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
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 15/6/25 下午7:29
 */
public abstract class AbstractEnumCodeTypeHandler<T extends Enum<?> & Codeable> extends BaseTypeHandler<T> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractEnumCodeTypeHandler.class);
    private final T[] enums;

    public AbstractEnumCodeTypeHandler(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("enum type can't be null!");
        }

        this.enums = clazz.getEnumConstants();
        if (this.enums == null) {
            logger.warn("This enum '{}' has none enum contents!", clazz.getSimpleName());
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getCode());
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
            if (em.getCode().equals(value)) {
                return em;
            }
        }
        return null;
    }
}

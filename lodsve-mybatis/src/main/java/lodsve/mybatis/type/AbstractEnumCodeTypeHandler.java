/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

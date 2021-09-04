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
package lodsve.mybatis.utils;

import lodsve.mybatis.repository.bean.EntityTable;
import lodsve.mybatis.repository.bean.VersionColumn;
import org.apache.commons.collections.MapUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.persistence.NoResultException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 乐观锁插件工具类.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public class VersionUtils {
    private static final Logger logger = LoggerFactory.getLogger(VersionUtils.class);
    private static final String VERSION_SQL = "SELECT %s FROM %s WHERE %s";

    /**
     * 获取当前版本号
     *
     * @param mappedStatement mappedStatement
     * @param table           表对象
     * @param keyMap          查询条件组合(property name in domain object -> value)
     * @param <T>             版本号的类型
     * @return 当前版本号
     * @throws SQLException sql执行异常
     */
    @SuppressWarnings("unchecked")
    public static <T> T getCurrentVersion(MappedStatement mappedStatement, EntityTable table, Map<String, Object> keyMap) throws SQLException {
        if (MapUtils.isEmpty(keyMap)) {
            return null;
        }

        VersionColumn version = table.getVersionColumn();
        String whereSql = keyMap.keySet().stream().map(key -> SqlUtils.camelHumpToUnderline(key).toUpperCase() + " = ?").collect(Collectors.joining(" AND "));

        String versionSql = String.format(VERSION_SQL, version.getColumn(), table.getName(), whereSql);

        ResultSet rs = null;
        Configuration configuration = mappedStatement.getConfiguration();

        Connection connection = DataSourceUtils.getConnection(configuration.getEnvironment().getDataSource());
        try (PreparedStatement versionStmt = connection.prepareStatement(versionSql)) {
            AtomicInteger i = new AtomicInteger();
            keyMap.forEach((key, value) -> {
                ParameterMapping parameterMapping = new ParameterMapping.Builder(configuration, key, value.getClass()).build();
                TypeHandler typeHandler = parameterMapping.getTypeHandler();
                try {
                    typeHandler.setParameter(versionStmt, i.get() + 1, value, parameterMapping.getJdbcType());
                    i.getAndIncrement();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            });

            rs = versionStmt.executeQuery();
            Object currentVersion = null;
            if (rs.next()) {
                // 为了解决使用阿里Druid数据源时，getObject(int columnIndex, Class<T> type)会直接抛SQLFeatureNotSupportedException这个异常的问题
                // 已经在GitHub上提了issue，不知道有没有回复 https://github.com/alibaba/druid/issues/2867
                currentVersion = rs.getObject(1, Collections.singletonMap(version.getColumn(), version.getJavaType()));
            }

            if (null == currentVersion) {
                throw new NoResultException(String.format("Can't find a record for '%s' in table '%s'!", keyMap.keySet().stream().map(k -> SqlUtils.camelHumpToUnderline(k).toUpperCase() + " = " + keyMap.get(k)).collect(Collectors.joining(",")), table.getName()));
            }

            return (T) currentVersion;
        } catch (SQLException e) {
            logger.error("查询当前version出错", e);
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("exception happens when doing: ResultSet.close()", e);
                }
            }
            DataSourceUtils.releaseConnection(connection, configuration.getEnvironment().getDataSource());
        }
    }
}

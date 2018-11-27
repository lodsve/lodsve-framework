/*
 * Copyright (C) 2018  Sun.Hao
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.mybatis.utils;

import lodsve.core.utils.StringUtils;
import lodsve.mybatis.dialect.Dialect;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * mybatis分页工具类.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 15/6/29 下午2:55
 */
public class PaginationUtils {
    private static final Logger logger = LoggerFactory.getLogger(PaginationUtils.class);
    private static final Pattern ORDER_BY = Pattern.compile(".*order\\s+by\\s+.*", Pattern.CASE_INSENSITIVE);

    private PaginationUtils() {
    }

    /**
     * 从MappedStatement的参数中指定类型的参数
     *
     * @param parameter
     * @param target
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T findObjectFromParameter(Object parameter, Class<T> target) {
        if (parameter == null || target == null) {
            return null;
        }

        if (target.isAssignableFrom(parameter.getClass())) {
            return (T) parameter;
        }

        if (parameter instanceof MapperMethod.ParamMap) {
            MapperMethod.ParamMap<Object> paramMap = (MapperMethod.ParamMap<Object>) parameter;
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                Object paramValue = entry.getValue();

                if (paramValue != null && target.isAssignableFrom(paramValue.getClass())) {
                    return (T) paramValue;
                }
            }
        }

        return null;
    }

    public static int queryForTotal(String sql, MappedStatement mappedStatement, BoundSql boundSql) throws SQLException {
        if (StringUtils.isEmpty(sql)) {
            return 0;
        }

        Connection connection = null;
        PreparedStatement preStmt = null;
        ResultSet rs = null;

        try {
            connection = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();

            Dialect dialect = MyBatisUtils.getDialect(connection);
            String totalSql = dialect.getCountSql(sql);

            preStmt = connection.prepareStatement(totalSql);
            BoundSql countBoundSql = copyFromBoundSql(mappedStatement, boundSql, totalSql);
            setParameters(preStmt, mappedStatement, countBoundSql, boundSql.getParameterObject());

            rs = preStmt.executeQuery();
            int totalCount = 0;
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }

            return totalCount;
        } catch (SQLException e) {
            logger.error("查询总记录数出错", e);
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("exception happens when doing: ResultSet.close()", e);
                }
            }

            if (preStmt != null) {
                try {
                    preStmt.close();
                } catch (SQLException e) {
                    logger.error("exception happens when doing: PreparedStatement.close()", e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("exception happens when doing: Connection.close()", e);
                }
            }
        }
    }

    public static String getPageSql(String sql, MappedStatement mappedStatement, int start, int num) throws SQLException {
        Assert.hasText(sql, "sql is required!");

        Dialect dialect;
        try (Connection connection = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection()) {
            dialect = MyBatisUtils.getDialect(connection);
        }

        return dialect.getPageSql(sql, start, num);
    }

    public static MappedStatement copyFromNewSql(MappedStatement ms, BoundSql boundSql, String sql) {
        BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, sql);

        return copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
    }

    private static BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        return newBoundSql;
    }

    /**
     * 对SQL参数(?)设值
     *
     * @param ps              PreparedStatement
     * @param mappedStatement MappedStatement
     * @param boundSql        BoundSql
     * @param parameterObject parameterObject
     * @throws SQLException SQLException
     */
    private static void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) throws SQLException {
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
        parameterHandler.setParameters(ps);
    }

    private static MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        Builder builder = new Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }

        //setStatementTimeout()
        builder.timeout(ms.getTimeout());

        //setStatementResultMap()
        builder.parameterMap(ms.getParameterMap());

        //setStatementResultMap()
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());

        //setStatementCache()
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    private static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    public static String applySortSql(String sql, Sort sort) {
        if (null == sort || !sort.iterator().hasNext()) {
            return sql;
        }

        StringBuilder builder = new StringBuilder(sql);

        if (!ORDER_BY.matcher(sql).matches()) {
            builder.append(" order by ");
        } else {
            builder.append(", ");
        }

        for (Sort.Order order : sort) {
            builder.append(getOrderClause(order)).append(", ");
        }

        builder.delete(builder.length() - 2, builder.length());

        return builder.toString();
    }

    private static String getOrderClause(Sort.Order order) {
        String property = order.getProperty();
        String wrapped = order.isIgnoreCase() ? String.format("lower(%s)", property) : property;

        return String.format("%s %s", wrapped, toSqlDirection(order));
    }

    private static String toSqlDirection(Sort.Order order) {
        return order.getDirection().name().toLowerCase(Locale.US);
    }
}

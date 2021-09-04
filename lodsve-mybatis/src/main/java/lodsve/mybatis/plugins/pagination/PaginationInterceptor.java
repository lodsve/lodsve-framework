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
package lodsve.mybatis.plugins.pagination;

import lodsve.mybatis.utils.PaginationUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * mybatis分页使用的拦截器.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 15/6/25 下午7:28
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class
        })
})
public class PaginationInterceptor implements Interceptor {
    private static final Integer MAPPED_STATEMENT_INDEX = 0;
    private static final Integer PARAMETER_INDEX = 1;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] queryArgs = invocation.getArgs();
        Object parameter = queryArgs[PARAMETER_INDEX];

        //在参数中获取分页的信息
        Pageable pageable = PaginationUtils.findObjectFromParameter(parameter, Pageable.class);
        Sort sort = PaginationUtils.findObjectFromParameter(parameter, Sort.class);
        if (pageable == null && sort == null) {
            //无需分页
            return invocation.proceed();
        }

        final MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
        final BoundSql boundSql = ms.getBoundSql(parameter);
        String sql = boundSql.getSql();

        if (pageable == null) {
            // 仅排序
            String orderSql = PaginationUtils.applySortSql(sql, sort);
            queryArgs[MAPPED_STATEMENT_INDEX] = PaginationUtils.copyFromNewSql(ms, boundSql, orderSql);

            return invocation.proceed();
        }

        int total = PaginationUtils.queryForTotal(sql, ms, boundSql);

        //参数sort优先于pageable中的sort
        if (sort == null && pageable.getSort() != null) {
            sort = pageable.getSort();
        }
        if (sort != null) {
            sql = PaginationUtils.applySortSql(sql, sort);
        }

        //分页语句
        String pageSql = PaginationUtils.getPageSql(sql, pageable.getOffset(), pageable.getPageSize());

        queryArgs[MAPPED_STATEMENT_INDEX] = PaginationUtils.copyFromNewSql(ms, boundSql, pageSql);
        queryArgs[2] = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);

        Object ret = invocation.proceed();
        Page<?> pi = new PageImpl<>((List<?>) ret, pageable, total);

        List<Page<?>> result = new ArrayList<>(1);
        result.add(pi);

        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}

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

package lodsve.mybatis.plugins.repository;

import lodsve.mybatis.repository.BaseRepository;
import lodsve.mybatis.repository.bean.*;
import lodsve.mybatis.repository.provider.BaseMapperProvider;
import lodsve.mybatis.repository.provider.ExternalProvider;
import lodsve.mybatis.utils.EntityUtils;
import lodsve.mybatis.utils.MapperUtils;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLSyntaxErrorException;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.ServiceLoader;

/**
 * 通用Mapper拦截器.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 15/7/13 下午4:31
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class BaseRepositoryInterceptor implements Interceptor {
    private final MapperUtils mapperUtils = new MapperUtils();
    private final static String LOGIC_DELETE_WITH_MODIFIED_BY_MAPPED_STATEMENT_ID = "logicDeleteWithModifiedBy";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] objects = invocation.getArgs();
        MappedStatement ms = (MappedStatement) objects[0];
        // 参数
        Object parameter = objects[1];
        String msId = ms.getId();
        //不需要拦截的方法直接返回
        if (mapperUtils.isMapperMethod(msId)) {
            // 第一次经过处理后，就不会是ProviderSqlSource了，一开始高并发时可能会执行多次，但不影响。以后就不会在执行了
            if (ms.getSqlSource() instanceof ProviderSqlSource) {
                mapperUtils.setSqlSource(ms, parameter);
            }
        }

        // 要排除logicDeleteWithModifiedBy这个方法，因为这个方法每次都会修改参数，所以得每次都从这边走
        if (msId.contains(LOGIC_DELETE_WITH_MODIFIED_BY_MAPPED_STATEMENT_ID)) {
            handleParams(ms, parameter);
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        mapperUtils.registerMapper(BaseRepository.class);

        ServiceLoader<ExternalProvider> serviceLoader = ServiceLoader.load(ExternalProvider.class);
        serviceLoader.iterator().forEachRemaining(obj -> obj.provider().stream().filter(Class::isInterface).forEach(mapperUtils::registerMapper));

        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private void handleParams(MappedStatement ms, Object parameter) throws SQLSyntaxErrorException {
        BaseMapperProvider mapperProvider = mapperUtils.getMapperProvider(ms.getId());

        Class<?> entityClass = mapperProvider.getSelectReturnType(ms);
        DeleteColumn deleteColumn = EntityUtils.getDeleteColumn(entityClass);
        if (null == deleteColumn) {
            throw new SQLSyntaxErrorException("不支持逻辑删除！没有@LogicDelete注解");
        }

        IdColumn idColumn = EntityUtils.getIdColumn(entityClass);
        LastModifiedByColumn modifiedByColumn = EntityUtils.getModifiedByColumn(entityClass);
        LastModifiedDateColumn modifiedDateColumn = EntityUtils.getModifiedDateColumn(entityClass);
        DisabledDateColumn disabledDateColumn = EntityUtils.getDisabledDateColumn(entityClass);

        handleParamMap(parameter, idColumn, modifiedByColumn, modifiedDateColumn, disabledDateColumn);
    }


    @SuppressWarnings("unchecked")
    private void handleParamMap(Object parameter, IdColumn idColumn, LastModifiedByColumn modifiedByColumn, LastModifiedDateColumn modifiedDateColumn, DisabledDateColumn disabledDateColumn) throws SQLSyntaxErrorException {
        // 修改参数
        if (!(parameter instanceof ParamMap)) {
            throw new SQLSyntaxErrorException("参数类型不正确！");
        }

        ParamMap<Object> paramMap = (ParamMap<Object>) parameter;
        Object id = ((ParamMap) parameter).get("arg0");
        Object modifiedBy = ((ParamMap) parameter).get("arg1");
        paramMap.remove("arg0");
        paramMap.remove("arg1");
        paramMap.remove("param1");
        paramMap.remove("param2");
        LocalDateTime now = LocalDateTime.now();

        if (modifiedByColumn != null) {
            paramMap.put(modifiedByColumn.getProperty(), modifiedBy);
        }
        if (modifiedDateColumn != null) {
            paramMap.put(modifiedDateColumn.getProperty(), now);
        }
        if (disabledDateColumn != null) {
            paramMap.put(disabledDateColumn.getProperty(), now);
        }
        paramMap.put(idColumn.getProperty(), id);
    }
}

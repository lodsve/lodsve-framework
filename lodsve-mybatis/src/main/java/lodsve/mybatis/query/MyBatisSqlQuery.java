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
package lodsve.mybatis.query;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 基于mybatis的查询.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public class MyBatisSqlQuery extends SqlQuery {
    private final MappedStatement mappedStatement;
    private PreparedStatement statement;
    private ResultSet resultSet;

    public MyBatisSqlQuery(MappedStatement mappedStatement) {
        super(mappedStatement.getConfiguration().getEnvironment().getDataSource());

        this.mappedStatement = mappedStatement;
    }

    public int queryForInt(BoundSql boundSql) throws SQLException {
        statement = connection.prepareStatement(boundSql.getSql());

        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, boundSql.getParameterObject(), boundSql);
        parameterHandler.setParameters(statement);

        resultSet = statement.executeQuery();
        int totalCount = 0;
        if (resultSet.next()) {
            totalCount = resultSet.getInt(1);
        }

        return totalCount;
    }

    @Override
    public void closeResources() throws SQLException {
        if (null != statement) {
            statement.close();
        }

        if (null != resultSet) {
            resultSet.close();
        }
    }
}

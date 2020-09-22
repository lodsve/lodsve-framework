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

/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
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

package lodsve.mybatis.query;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQL查询，使用try-with-resource可以自动关闭连接.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public class NativeSqlQuery extends SqlQuery {
    private PreparedStatement statement;
    private ResultSet resultSet;

    public NativeSqlQuery(DataSource dataSource) {
        super(dataSource);
    }

    public int queryForInt(String sql, Object... params) throws SQLException {
        statement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }

        resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        } else {
            return -1;
        }
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

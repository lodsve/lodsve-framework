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

package lodsve.mybatis.key.oracle;

import lodsve.mybatis.exception.MyBatisException;
import lodsve.mybatis.key.IDGenerator;
import lodsve.mybatis.utils.MyBatisUtils;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * oracle主键.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/9/14 上午9:57
 */
public class OracleIDGenerator implements IDGenerator {
    private DataSource dataSource;

    @Override
    public synchronized Long nextId(String sequenceName) {
        Assert.hasText(sequenceName);

        String sql = String.format("SELECT %s.NEXTVAL FROM DUAL", sequenceName);

        try {
            return (long) MyBatisUtils.queryForInt(dataSource, sql);
        } catch (SQLException e) {
            throw new MyBatisException(102003, e.getMessage());
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}

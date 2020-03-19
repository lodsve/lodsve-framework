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
package lodsve.mybatis.commons;

import lodsve.mybatis.repository.bean.EntityTable;
import lodsve.mybatis.repository.helper.EntityHelper;
import lodsve.mybatis.repository.provider.BaseMapperProvider;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * .
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public class TestBaseDaoMapperProvider extends BaseMapperProvider {
    public TestBaseDaoMapperProvider(Class<?> mapperClass) {
        super(mapperClass);
    }

    public String listAll(MappedStatement ms) {
        EntityTable table = EntityHelper.getEntityTable(getSelectReturnType(ms));

        return "SELECT * FROM " + table.getName();
    }
}

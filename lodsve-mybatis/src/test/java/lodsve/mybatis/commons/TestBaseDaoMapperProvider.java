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
package lodsve.mybatis.commons;

import lodsve.mybatis.repository.bean.ColumnBean;
import lodsve.mybatis.repository.helper.EntityHelper;
import lodsve.mybatis.repository.provider.BaseMapperProvider;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.xmltags.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * .
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public class TestBaseDaoMapperProvider extends BaseMapperProvider {
    public TestBaseDaoMapperProvider(Class<?> mapperClass) {
        super(mapperClass);
    }

    public SqlNode batchSave2(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<SqlNode> sqlNodes = new LinkedList<>();
        //insert into table
        sqlNodes.add(new StaticTextSqlNode("INSERT INTO " + EntityHelper.getEntityTable(entityClass).getName()));

        //获取全部列
        Set<ColumnBean> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> columnNodes = new LinkedList<>();

        for (ColumnBean column : columnList) {
            columnNodes.add(new StaticTextSqlNode(column.getColumn() + ","));
        }
        //将动态的列加入sqlNodes
        sqlNodes.add(new TrimSqlNode(ms.getConfiguration(), new MixedSqlNode(columnNodes), "(", null, ")", ","));

        List<SqlNode> valuesNodes = new LinkedList<>();

        //处理values(#{property},#{property}...)
        // 1. 组装每一个value
        for (ColumnBean column : columnList) {
            valuesNodes.add(new StaticTextSqlNode("#{item." + column.getProperty() + "},"));
        }
        SqlNode eachValueSqlNode = new TrimSqlNode(ms.getConfiguration(), new MixedSqlNode(valuesNodes), "(", null, ")", ",");

        // 2. 组装(#{property1},#{property2}...),(#{property3},#{property4}...)
        SqlNode valuesSqlNode = new ForEachSqlNode(ms.getConfiguration(), eachValueSqlNode, "list", "index", "item", "", "", ",");

        // 3. 合并成 values(#{property1},#{property2}...),(#{property3},#{property4}...)
        sqlNodes.add(new MixedSqlNode(Arrays.asList(new StaticTextSqlNode(" VALUES"), valuesSqlNode)));

        // 通过反射设置主键字段
        setKeyColumn(EntityHelper.getIdColumn(entityClass), ms);
        return new MixedSqlNode(sqlNodes);
    }
}

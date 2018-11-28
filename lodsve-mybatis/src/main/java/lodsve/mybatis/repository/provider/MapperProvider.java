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

package lodsve.mybatis.repository.provider;

import lodsve.mybatis.repository.BaseRepository;
import lodsve.mybatis.repository.annotations.LogicDelete;
import lodsve.mybatis.repository.bean.ColumnBean;
import lodsve.mybatis.repository.bean.DeleteColumn;
import lodsve.mybatis.repository.bean.EntityTable;
import lodsve.mybatis.repository.bean.IdColumn;
import lodsve.mybatis.utils.EntityUtils;
import lodsve.mybatis.utils.MapperUtils;
import org.apache.ibatis.mapping.MappedStatement;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper实现类，基础方法实现类.其中的方法与{@link lodsve.mybatis.repository.BaseRepository}一一对应.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @see lodsve.mybatis.repository.BaseRepository
 */
public class MapperProvider extends BaseMapperProvider {

    public MapperProvider(Class<?> mapperClass, MapperUtils mapperUtils) {
        super(mapperClass, mapperUtils);
    }

    /**
     * 根据实体中的id属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
     *
     * @param ms MappedStatement
     * @return 根据实体中的id属性进行查询Sql
     * @see BaseRepository#findById(Serializable)
     */
    public String findById(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        EntityTable table = EntityUtils.getEntityTable(entityClass);
        IdColumn idColumn = table.getIdColumn();
        Set<ColumnBean> columnBeans = table.getColumns();

        String columns = columnBeans.stream().map(ColumnBean::getColumn).collect(Collectors.joining(", "));

        return String.format("SELECT %s FROM %s WHERE %s", columns, table.getName(), idColumn.getColumn() + " = #{" + idColumn.getProperty() + "}");
    }

    /**
     * 根据实体中的id属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号<br/>
     * 这个方法只获取加了{@link LogicDelete}注解的字段值为{@link LogicDelete#nonDelete()}的记录<br/>
     * 如果没有加这个注解，这个方法的作用与{@link BaseRepository#findById(Serializable)}一致！
     *
     * @param ms MappedStatement
     * @return 生成的SQL语句
     * @see BaseRepository#findEnabledById(Serializable)
     */
    public String findEnabledById(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        EntityTable table = EntityUtils.getEntityTable(entityClass);
        IdColumn idColumn = table.getIdColumn();
        DeleteColumn deleteColumn = table.getDeleteColumn();
        Set<ColumnBean> columnBeans = table.getColumns();

        String columns = columnBeans.stream().map(ColumnBean::getColumn).collect(Collectors.joining(", "));

        String whereSql = idColumn.getColumn() + " = #{" + idColumn.getProperty() + "}";
        if (deleteColumn != null) {
            whereSql += " AND " + deleteColumn.getColumn() + " = " + deleteColumn.getNonDelete();
        }

        return String.format("SELECT %s FROM %s WHERE %s", columns, table.getName(), whereSql);
    }

    /**
     * 根据传入的主键集合，查询出对象的集合(不会按照软删除来查询，查询条件仅仅为主键匹配)
     *
     * @param ms MappedStatement
     * @return 生成的SQL语句
     * @see BaseRepository#findByIds(List)
     */
    public String findByIds(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        EntityTable table = EntityUtils.getEntityTable(entityClass);
        IdColumn idColumn = table.getIdColumn();
        Set<ColumnBean> columnBeans = table.getColumns();

        String columns = columnBeans.stream().map(ColumnBean::getColumn).collect(Collectors.joining(", "));
        String idIns = "<foreach collection=\"list\" item=\"item\" separator=\",\" open=\"(\" close=\")\">#{item}</foreach>";

        return String.format("SELECT %s FROM %s WHERE %s IN %s", columns, table.getName(), idColumn.getColumn(), idIns);
    }

    /**
     * 根据实体中的id属性进行查询，查询出对象的集合查询条件使用等号<br/>
     * 这个方法只获取加了{@link LogicDelete}注解的字段值为{@link LogicDelete#nonDelete()}的记录<br/>
     * 如果没有加这个注解，这个方法的作用与{@link #findByIds(MappedStatement)} 一致！
     *
     * @param ms MappedStatement
     * @return 生成的SQL语句
     * @see BaseRepository#findEnabledByIds(List)
     */
    public String findEnabledByIds(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        EntityTable table = EntityUtils.getEntityTable(entityClass);
        IdColumn idColumn = table.getIdColumn();
        DeleteColumn deleteColumn = table.getDeleteColumn();
        Set<ColumnBean> columnBeans = table.getColumns();

        String columns = columnBeans.stream().map(ColumnBean::getColumn).collect(Collectors.joining(", "));
        String idIns = "<foreach collection=\"list\" item=\"item\" separator=\",\" open=\"(\" close=\")\">#{item}</foreach>";

        String whereSql = idColumn.getColumn() + " IN " + idIns;
        if (deleteColumn != null) {
            whereSql += " AND " + deleteColumn.getColumn() + " = " + deleteColumn.getNonDelete();
        }

        return String.format("SELECT %s FROM %s WHERE %s", columns, table.getName(), whereSql);
    }

    /**
     * 保存一个实体，null的属性不会保存，会使用数据库默认值。
     *
     * @param ms MappedStatement
     * @return 生成的SQL语句
     * @see BaseRepository#save(Object)
     */
    public String save(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        EntityTable table = EntityUtils.getEntityTable(entityClass);
        Set<ColumnBean> columnBeans = table.getColumns();

        String columns = columnBeans.stream().map(ColumnBean::getColumn).collect(Collectors.joining(", "));
        String values = columnBeans.stream().map(c -> "#{" + c.getProperty() + "}").collect(Collectors.joining(", "));

        // 通过反射设置主键字段
        setKeyColumn(table.getIdColumn(), ms);

        return String.format("INSERT INTO %s (%s) VALUES (%s)", table.getName(), columns, values);
    }

    /**
     * 批量保存，保存后生成的主键会回填到每一个对象的主键字段
     *
     * @param ms MappedStatement
     * @return 生成的SQL语句
     * @see BaseRepository#batchSave(List)
     */
    public String batchSave(MappedStatement ms) {
        return "";
    }

    /**
     * 根据主键更新所有属性的值。
     *
     * @param ms MappedStatement
     * @return 生成的SQL语句
     * @see BaseRepository#updateAll(Object)
     */
    public String updateAll(MappedStatement ms) {
        return "";
    }

    /**
     * 根据主键更新属性不为null的值（String类型，应该还不为空字符串）。
     *
     * @param ms MappedStatement
     * @return 生成的SQL语句
     * @see BaseRepository#update(Object)
     */
    public String update(MappedStatement ms) {
        return "";
    }

    /**
     * 根据主键字段进行删除，方法参数必须包含完整的主键属性！<p/>
     * 注意：这里是物理删除，慎用！
     *
     * @param ms MappedStatement
     * @return 生成的SQL语句
     * @see BaseRepository#deleteById(Serializable)
     */
    public String deleteById(MappedStatement ms) {
        return "";
    }

    /**
     * 逻辑删除，需要在逻辑删除字段添加注解{@link LogicDelete}
     *
     * @param ms MappedStatement
     * @return 生成的SQL语句
     * @see BaseRepository#logicDeleteById(Serializable)
     */
    public String logicDeleteById(MappedStatement ms) {
        return "";
    }

    /**
     * 逻辑删除，需要在逻辑删除字段添加注解{@link LogicDelete}.
     *
     * @param ms MappedStatement
     * @return 生成的SQL语句
     * @see BaseRepository#logicDeleteByIdWithModifiedBy(Serializable, Long)
     */
    public String logicDeleteByIdWithModifiedBy(MappedStatement ms) {
        return "";
    }

    /**
     * 查询总条数
     *
     * @param ms MappedStatement
     * @return 生成的SQL语句
     * @see BaseRepository#count()
     */
    public String count(MappedStatement ms) {
        return "";
    }

    /**
     * 查询总条数
     * 这个方法只获取加了{@link LogicDelete}注解的字段值为{@link LogicDelete#nonDelete()}的记录<br/>
     * 如果没有加这个注解，这个方法的作用与{@link #count(MappedStatement)}一致！
     *
     * @param ms MappedStatement
     * @return 生成的SQL语句
     * @see BaseRepository#countEnabled()
     */
    public String countEnabled(MappedStatement ms) {
        return "";
    }

    /**
     * 判断是否存在
     *
     * @param ms MappedStatement
     * @return 生成的SQL语句
     * @see BaseRepository#isExist(Serializable)
     */
    public String isExist(MappedStatement ms) {
        return "";
    }

    /**
     * 判断是否存在(如果有逻辑删除，则添加这个条件，否则与{@link #isExist(MappedStatement)})效果一致
     *
     * @param ms MappedStatement
     * @return 生成的SQL语句
     * @see BaseRepository#isExistEnabled(Serializable)
     */
    public String isExistEnabled(MappedStatement ms) {
        return "";
    }
}

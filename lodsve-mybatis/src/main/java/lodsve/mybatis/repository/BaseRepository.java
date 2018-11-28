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

package lodsve.mybatis.repository;

import lodsve.mybatis.repository.annotations.LogicDelete;
import lodsve.mybatis.repository.provider.MapperProvider;
import lodsve.mybatis.utils.MapperUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.MappedStatement;

import java.io.Serializable;
import java.util.List;

/**
 * 基础dao,将常用的数据库CRUD方法放在这里,需要用到时,只需直接继承此接口就好了.<br/>
 * 其中的方法与{@link MapperProvider}一一对应<br/>
 * eg:<br/>
 * <pre>
 *  &#64;Repository
 *  public interface DemoDAO extends BaseRepository<Demo> {
 *  }
 * </pre>
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @see MapperProvider
 */
public interface BaseRepository<T> {
    /**
     * 根据实体中的id属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
     *
     * @param id 主键值
     * @return 查询到的值
     * @see MapperProvider#findById(MappedStatement)
     */
    @SelectProvider(type = MapperProvider.class, method = MapperUtils.PROVIDER_METHOD_NAME)
    T findById(Serializable id);

    /**
     * 根据实体中的id属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号<br/>
     * 这个方法只获取加了{@link LogicDelete}注解的字段值为{@link LogicDelete#nonDelete()}的记录<br/>
     * 如果没有加这个注解，这个方法的作用与{@link #findById(Serializable)}一致！
     *
     * @param id 主键值
     * @return 查询到的值
     * @see MapperProvider#findEnabledById(MappedStatement)
     */
    @SelectProvider(type = MapperProvider.class, method = MapperUtils.PROVIDER_METHOD_NAME)
    T findEnabledById(Serializable id);

    /**
     * 根据传入的主键集合，查询出对象的集合(不会按照软删除来查询，查询条件仅仅为主键匹配)
     *
     * @param ids 主键集合
     * @return 主键匹配的对象的集合
     * @see MapperProvider#findByIds(MappedStatement)
     */
    @SelectProvider(type = MapperProvider.class, method = MapperUtils.PROVIDER_METHOD_NAME)
    List<T> findByIds(List<? extends Serializable> ids);

    /**
     * 根据实体中的id属性进行查询，查询出对象的集合查询条件使用等号<br/>
     * 这个方法只获取加了{@link LogicDelete}注解的字段值为{@link LogicDelete#nonDelete()}的记录<br/>
     * 如果没有加这个注解，这个方法的作用与{@link #findById(Serializable)}一致！
     *
     * @param ids 主键集合
     * @return 主键匹配的对象的集合
     * @see MapperProvider#findEnabledByIds(MappedStatement)
     */
    @SelectProvider(type = MapperProvider.class, method = MapperUtils.PROVIDER_METHOD_NAME)
    List<T> findEnabledByIds(List<? extends Serializable> ids);

    /**
     * 保存一个实体，null的属性不会保存，会使用数据库默认值。
     *
     * @param entity 需要保存的对象(主键为空则自动生成主键值,否则使用原主键值)
     * @return 操作后影响的数据库记录数量(一般情况为1)
     * @see MapperProvider#save(MappedStatement)
     */
    @InsertProvider(type = MapperProvider.class, method = MapperUtils.PROVIDER_METHOD_NAME)
    @Options(useGeneratedKeys = true)
    int save(T entity);

    /**
     * 批量保存，保存后生成的主键会回填到每一个对象的主键字段
     *
     * @param entities 需要保存对象的集合
     * @return 保存成功的数据条数
     * @see MapperProvider#batchSave(MappedStatement)
     */
    @InsertProvider(type = MapperProvider.class, method = MapperUtils.PROVIDER_METHOD_NAME)
    @Options(useGeneratedKeys = true)
    int batchSave(List<T> entities);

    /**
     * 根据主键更新属性不为null的值。
     *
     * @param entity 需要更新的对象,必须含有主键值
     * @return 操作后影响的数据库记录数量(一般情况为1)
     * @see MapperProvider#updateAll(MappedStatement)
     */
    @UpdateProvider(type = MapperProvider.class, method = MapperUtils.PROVIDER_METHOD_NAME)
    int updateAll(T entity);

    /**
     * 根据主键更新属性不为null的值。
     *
     * @param entity 需要更新的对象,必须含有主键值
     * @return 操作后影响的数据库记录数量(一般情况为1)
     * @see MapperProvider#update(MappedStatement)
     */
    @UpdateProvider(type = MapperProvider.class, method = MapperUtils.PROVIDER_METHOD_NAME)
    int update(T entity);

    /**
     * 根据主键字段进行删除，方法参数必须包含完整的主键属性
     *
     * @param id 主键
     * @return 操作结果
     * @see MapperProvider#deleteById(MappedStatement)
     */
    @DeleteProvider(type = MapperProvider.class, method = MapperUtils.PROVIDER_METHOD_NAME)
    boolean deleteById(Serializable id);

    /**
     * 逻辑删除，需要在逻辑删除字段添加注解{@link LogicDelete}.
     *
     * @param id 主键
     * @return 操作结果
     * @see MapperProvider#logicDeleteById(MappedStatement)
     */
    @UpdateProvider(type = MapperProvider.class, method = MapperUtils.PROVIDER_METHOD_NAME)
    boolean logicDeleteById(Serializable id);

    /**
     * 逻辑删除，需要在逻辑删除字段添加注解{@link LogicDelete}.
     *
     * @param id             主键
     * @param lastModifiedBy 更新人字段
     * @return 操作结果
     * @see MapperProvider#logicDeleteByIdWithModifiedBy(MappedStatement)
     */
    @UpdateProvider(type = MapperProvider.class, method = MapperUtils.PROVIDER_METHOD_NAME)
    boolean logicDeleteByIdWithModifiedBy(Serializable id, Long lastModifiedBy);

    /**
     * 查询总条数
     *
     * @return 数据库记录总条数
     * @see MapperProvider#count(MappedStatement)
     */
    @SelectProvider(type = MapperProvider.class, method = MapperUtils.PROVIDER_METHOD_NAME)
    int count();

    /**
     * 查询总条数
     * 这个方法只获取加了{@link LogicDelete}注解的字段值为{@link LogicDelete#nonDelete()}的记录<br/>
     * 如果没有加这个注解，这个方法的作用与{@link #count()}一致！
     *
     * @return 数据库记录总条数
     * @see MapperProvider#countEnabled(MappedStatement)
     */
    @SelectProvider(type = MapperProvider.class, method = MapperUtils.PROVIDER_METHOD_NAME)
    int countEnabled();

    /**
     * 判断是否存在
     *
     * @param id 主键值
     * @return 是否存在
     */
    @SelectProvider(type = MapperProvider.class, method = MapperUtils.PROVIDER_METHOD_NAME)
    boolean isExist(Serializable id);

    /**
     * 判断是否存在(如果有逻辑删除，则添加这个条件，否则与{@link #isExist(Serializable)})效果一致
     *
     * @param id 主键值
     * @return 是否存在
     */
    @SelectProvider(type = MapperProvider.class, method = MapperUtils.PROVIDER_METHOD_NAME)
    boolean isExistEnabled(Serializable id);
}

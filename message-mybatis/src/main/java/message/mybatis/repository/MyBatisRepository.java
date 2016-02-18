package message.mybatis.repository;

import message.mybatis.repository.provider.MapperProvider;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.io.Serializable;

/**
 * 基础dao,将常用的数据库CRUD方法放在这里,需要用到时,只需直接继承此接口就好了.<br/>
 * 其中的方法与{@code message.mybatis.repository.dao.BaseRepository}一一对应<br/>
 * eg:<br/>
 * <pre>
 *  &#64;Repository
 *  public interface DemoDAO extends BaseRepository<Demo> {
 *  }
 * </pre>
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/7/13 下午3:39
 * @see message.mybatis.repository.provider.MapperProvider
 */
public interface MyBatisRepository<T> {
    /**
     * 根据实体中的id属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
     *
     * @param id 主键值
     * @return 查询到的值
     */
    @SelectProvider(type = MapperProvider.class, method = "dynamicSQL")
    T findOne(Serializable id);

    /**
     * 根据实体中的属性进行查询，只能有一个返回值
     *
     * @param entity 条件封装成对象
     * @return 查询到的值
     */
    @SelectProvider(type = MapperProvider.class, method = "dynamicSQL")
    T findByEntity(T entity);

    /**
     * 保存一个实体，null的属性不会保存，会使用数据库默认值。
     *
     * @param entity 需要保存的对象(主键为空则自动生成主键值,否则使用原主键值)
     * @return 操作后影响的数据库记录数量(一般情况为1)
     */
    @InsertProvider(type = MapperProvider.class, method = "dynamicSQL")
    int save(T entity);

    /**
     * 根据主键更新属性不为null的值。
     *
     * @param entity 需要更新的对象,必须含有主键值
     * @return 操作后影响的数据库记录数量(一般情况为1)
     */
    @UpdateProvider(type = MapperProvider.class, method = "dynamicSQL")
    int update(T entity);

    /**
     * 根据主键字段进行删除，方法参数必须包含完整的主键属性
     *
     * @param id 主键
     * @return 操作后影响的数据库记录数量(一般情况为1)
     */
    @DeleteProvider(type = MapperProvider.class, method = "dynamicSQL")
    boolean delete(Serializable id);
}

package message.mybatis.common.dao;

import message.mybatis.common.provider.MapperProvider;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.io.Serializable;

/**
 * 基础dao,将常用的数据库CRUD方法放在这里,需要用到时,只需直接继承此接口就好了.<br/>
 * 其中的方法与{@code message.mybatis.common.dao.BaseRepository}一一对应<br/>
 * eg:<br/>
 * <pre>
 *  &#64;Repository
 *  public interface DemoDAO extends BaseRepository<Demo> {
 *  }
 * </pre>
 *
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/7/13 下午3:39
 * @see message.mybatis.common.provider.MapperProvider
 */
public interface BaseRepository<T> {
    /**
     * 根据实体中的id属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
     *
     * @param id
     * @return
     */
    @SelectProvider(type = MapperProvider.class, method = "dynamicSQL")
    T select(Serializable id);

    /**
     * 保存一个实体，null的属性不会保存，会使用数据库默认值。
     *
     * @param t
     * @return
     */
    @InsertProvider(type = MapperProvider.class, method = "dynamicSQL")
    int inert(T t);

    /**
     * 根据主键更新属性不为null的值。
     *
     * @param entity
     * @return
     */
    @UpdateProvider(type = MapperProvider.class, method = "dynamicSQL")
    int update(T entity);

    /**
     * 根据主键字段进行删除，方法参数必须包含完整的主键属性
     *
     * @param id
     * @return
     */
    @DeleteProvider(type = MapperProvider.class, method = "dynamicSQL")
    int delete(Serializable id);
}

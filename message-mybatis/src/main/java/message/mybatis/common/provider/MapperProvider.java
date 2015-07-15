package message.mybatis.common.provider;

import message.mybatis.common.mapper.EntityHelper;
import message.mybatis.common.mapper.MapperHelper;
import message.mybatis.common.mapper.MapperTemplate;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.scripting.xmltags.IfSqlNode;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SetSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.StaticTextSqlNode;
import org.apache.ibatis.scripting.xmltags.TrimSqlNode;
import org.apache.ibatis.scripting.xmltags.WhereSqlNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Mappper实现类，基础方法实现类.其中的方法与{@code message.mybatis.common.dao.BaseRepository}一一对应
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/7/13 下午3:39
 * @see message.mybatis.common.dao.BaseRepository
 */
public class MapperProvider extends MapperTemplate {
    public MapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 根据实体中的id属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
     *
     * @param ms
     * @return
     */
    public SqlNode select(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        //修改返回值类型为实体类型
        setResultType(ms, entityClass);
        List<SqlNode> sqlNodes = new LinkedList<SqlNode>();
        //静态的sql部分:select column ... from table
        sqlNodes.add(new StaticTextSqlNode("SELECT "
                + EntityHelper.getSelectColumns(entityClass)
                + " FROM "
                + tableName(entityClass)));

        //获取全部的主键的列
        Set<EntityHelper.EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
        List<SqlNode> whereNodes = new LinkedList<SqlNode>();
        boolean first = true;
        //where 主键=#{property} 条件
        for (EntityHelper.EntityColumn column : columnList) {
            whereNodes.add(getColumnEqualsProperty(column, first));
            first = false;
        }
        sqlNodes.add(new WhereSqlNode(ms.getConfiguration(), new MixedSqlNode(whereNodes)));

        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 根据实体中的属性进行查询，只能有一个返回值
     *
     * @param ms
     * @return
     */
    public SqlNode selectOne(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        //修改返回值类型为实体类型
        setResultType(ms, entityClass);
        List<SqlNode> sqlNodes = new LinkedList<SqlNode>();
        //静态的sql部分:select column ... from table
        sqlNodes.add(new StaticTextSqlNode("SELECT "
                + EntityHelper.getSelectColumns(entityClass)
                + " FROM "
                + tableName(entityClass)));
        //将if添加到<where>
        sqlNodes.add(new WhereSqlNode(ms.getConfiguration(), getAllIfColumnNode(entityClass)));
        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 保存一个实体，null的属性不会保存，会使用数据库默认值。
     *
     * @param ms
     * @return
     */
    public SqlNode insert(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<SqlNode> sqlNodes = new LinkedList<SqlNode>();
        //insert into table
        sqlNodes.add(new StaticTextSqlNode("INSERT INTO " + tableName(entityClass)));
        //获取全部列
        Set<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new LinkedList<SqlNode>();
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityHelper.EntityColumn column : columnList) {
            ifNodes.add(getIfNotNull(column, new StaticTextSqlNode(column.getColumn() + ",")));
        }
        //将动态的列加入sqlNodes
        sqlNodes.add(new TrimSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes), "(", null, ")", ","));

        ifNodes = new LinkedList<SqlNode>();
        //处理values(#{property},#{property}...)
        for (EntityHelper.EntityColumn column : columnList) {
            ifNodes.add(new IfSqlNode(new StaticTextSqlNode("#{" + column.getProperty() + "},"), column.getProperty() + " != null "));
        }
        //values(#{property},#{property}...)
        sqlNodes.add(new TrimSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes), "VALUES (", null, ")", ","));
        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 根据主键更新属性不为null的值。
     *
     * @param ms
     */
    public SqlNode update(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<SqlNode> sqlNodes = new LinkedList<SqlNode>();
        //update table
        sqlNodes.add(new StaticTextSqlNode("UPDATE " + tableName(entityClass)));
        //获取全部列
        Set<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new LinkedList<SqlNode>();
        //全部的if property!=null and property!=''
        for (EntityHelper.EntityColumn column : columnList) {
            if (!column.isId()) {
                StaticTextSqlNode columnNode = new StaticTextSqlNode(column.getColumn() + " = #{" + column.getProperty() + "}, ");
                ifNodes.add(getIfNotNull(column, columnNode));
            }
        }
        sqlNodes.add(new SetSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes)));
        //获取全部的主键的列
        columnList = EntityHelper.getPKColumns(entityClass);
        List<SqlNode> whereNodes = new LinkedList<SqlNode>();
        boolean first = true;
        //where 主键=#{property} 条件
        for (EntityHelper.EntityColumn column : columnList) {
            whereNodes.add(getColumnEqualsProperty(column, first));
            first = false;
        }
        sqlNodes.add(new WhereSqlNode(ms.getConfiguration(), new MixedSqlNode(whereNodes)));
        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 根据主键字段进行删除，方法参数必须包含完整的主键属性
     *
     * @param ms
     */
    public void delete(MappedStatement ms) {
        final Class<?> entityClass = getSelectReturnType(ms);
        List<ParameterMapping> parameterMappings = getPrimaryKeyParameterMappings(ms);
        //开始拼sql
        String sql = new SQL() {{
            //delete from table
            DELETE_FROM(tableName(entityClass));
            //where 主键=#{property} 条件
            WHERE(EntityHelper.getPrimaryKeyWhere(entityClass));
        }}.toString();
        //静态SqlSource
        StaticSqlSource sqlSource = new StaticSqlSource(ms.getConfiguration(), sql, parameterMappings);
        //替换原有的SqlSource
        setSqlSource(ms, sqlSource);
    }
}

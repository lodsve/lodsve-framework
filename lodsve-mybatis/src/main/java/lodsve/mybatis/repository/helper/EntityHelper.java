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
package lodsve.mybatis.repository.helper;

import lodsve.core.utils.StringUtils;
import lodsve.mybatis.repository.annotations.DisabledDate;
import lodsve.mybatis.repository.annotations.LogicDelete;
import lodsve.mybatis.repository.bean.*;
import lodsve.mybatis.utils.SqlUtils;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 实体类工具类 - 处理实体和数据库表以及字段关键的一个类.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public class EntityHelper {
    /**
     * 实体类 => 表对象
     */
    private static final Map<Class<?>, EntityTable> ENTITY_TABLE_MAP = new HashMap<>();

    /**
     * 获取表对象
     *
     * @param entityClass entityClass
     * @return EntityTable
     */
    public static EntityTable getEntityTable(Class<?> entityClass) {
        EntityTable entityTable = ENTITY_TABLE_MAP.get(entityClass);
        if (entityTable == null) {
            initEntityNameMap(entityClass);
            entityTable = ENTITY_TABLE_MAP.get(entityClass);
        }
        if (entityTable == null) {
            throw new RuntimeException("无法获取实体类" + entityClass.getCanonicalName() + "对应的表名!");
        }
        return entityTable;
    }

    /**
     * 初始化实体属性
     *
     * @param entityClass entityClass
     */
    private static synchronized void initEntityNameMap(Class<?> entityClass) {
        if (ENTITY_TABLE_MAP.get(entityClass) != null) {
            return;
        }
        //表名
        EntityTable entityTable = new EntityTable();
        entityTable.setEntityType(entityClass);
        if (entityClass.isAnnotationPresent(Table.class)) {
            Table table = entityClass.getAnnotation(Table.class);
            if (StringUtils.isNotEmpty(table.name())) {
                entityTable.setName(table.name());
            } else {
                entityTable.setName(SqlUtils.camelHumpToUnderline(entityClass.getSimpleName()));
            }
        } else {
            throw new RuntimeException("类[" + entityClass.getName() + "]必须要有@Table注解!");
        }

        //列
        List<Field> fieldList = getAllField(entityClass, null);
        Set<ColumnBean> columnSet = new LinkedHashSet<>();
        for (Field field : fieldList) {
            handleEntityField(entityTable, field, columnSet);
        }

        if (entityTable.getIdColumn() == null) {
            throw new RuntimeException("类[" + entityClass.getName() + "]必须要有主键@Id!");
        }

        entityTable.setColumns(columnSet);

        //缓存
        ENTITY_TABLE_MAP.put(entityClass, entityTable);
    }

    private static void handleEntityField(EntityTable entityTable, Field field, Set<ColumnBean> columnSet) {
        //排除字段
        if (field.isAnnotationPresent(Transient.class)) {
            return;
        }
        ColumnBean columnBean = new ColumnBean(entityTable);

        String columnName = null;
        if (field.isAnnotationPresent(Column.class)) {
            Column column = field.getAnnotation(Column.class);
            columnName = column.name();
        }
        if (StringUtils.isEmpty(columnName)) {
            columnName = SqlUtils.camelHumpToUnderline(field.getName());
        }

        columnBean.setProperty(field.getName());
        columnBean.setColumn(columnName.toUpperCase());
        columnBean.setJavaType(field.getType());

        // 主键字段
        if (field.isAnnotationPresent(Id.class)) {
            IdColumn idColumn = new IdColumn(columnBean);
            //主键策略
            if (field.isAnnotationPresent(GeneratedValue.class)) {
                GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
                idColumn.setGenerator(generatedValue.generator());
            }

            entityTable.setIdColumn(idColumn);
        }

        // 乐观锁字段
        if (field.isAnnotationPresent(Version.class)) {
            VersionColumn versionColumn = new VersionColumn(columnBean);
            entityTable.setVersionColumn(versionColumn);
        }

        // 逻辑删除字段
        if (field.isAnnotationPresent(LogicDelete.class)) {
            LogicDelete delete = field.getAnnotation(LogicDelete.class);

            DeleteColumn deleteColumn = new DeleteColumn(columnBean);
            deleteColumn.setDelete(delete.delete());
            deleteColumn.setNonDelete(delete.nonDelete());
            entityTable.setDeleteColumn(deleteColumn);
        }

        // 更新人字段
        if (field.isAnnotationPresent(LastModifiedBy.class)) {
            LastModifiedByColumn modifiedByColumn = new LastModifiedByColumn(columnBean);
            entityTable.setModifiedByColumn(modifiedByColumn);
        }
        // 更新时间字段
        if (field.isAnnotationPresent(LastModifiedDate.class)) {
            LastModifiedDateColumn modifiedDateColumn = new LastModifiedDateColumn(columnBean);
            entityTable.setModifiedDateColumn(modifiedDateColumn);
        }
        // 禁用时间字段
        if (field.isAnnotationPresent(DisabledDate.class)) {
            DisabledDateColumn disabledDateColumn = new DisabledDateColumn(columnBean);
            entityTable.setDisabledDateColumn(disabledDateColumn);
        }

        columnSet.add(columnBean);
    }

    /**
     * 获取全部的Field
     *
     * @param entityClass entityClass
     * @param fieldList   fieldList
     * @return 全部的Field
     */
    private static List<Field> getAllField(Class<?> entityClass, List<Field> fieldList) {
        if (fieldList == null) {
            fieldList = new LinkedList<>();
        }
        if (entityClass.equals(Object.class)) {
            return fieldList;
        }
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            //排除静态字段
            if (!Modifier.isStatic(field.getModifiers())) {
                fieldList.add(field);
            }
        }
        Class<?> superClass = entityClass.getSuperclass();
        boolean result = superClass != null
                && !superClass.equals(Object.class)
                && (superClass.isAnnotationPresent(Entity.class)
                || (!Map.class.isAssignableFrom(superClass)
                && !Collection.class.isAssignableFrom(superClass)));
        if (result) {
            return getAllField(entityClass.getSuperclass(), fieldList);
        }
        return fieldList;
    }
}

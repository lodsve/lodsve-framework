package message.mybatis.common.mapper;

import message.mybatis.exception.MybatisException;
import message.utils.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 实体类工具类 - 处理实体和数据库表以及字段关键的一个类
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/7/13 下午3:39
 */
public class EntityHelper {

    /**
     * 实体对应表的配置信息
     */
    public static class EntityTable {
        private String name;
        //实体类 => 全部列属性
        private Set<EntityColumn> entityClassColumns;
        //实体类 => 主键信息
        private Set<EntityColumn> entityClassPKColumns;
        //字段名和属性名的映射
        private Map<String, String> aliasMap;
        //useGenerator包含多列的时候需要用到
        private List<String> keyProperties;
        private List<String> keyColumns;

        public void setTable(Table table) {
            this.name = table.name();
        }

        public String getName() {
            return name;
        }

        public Set<EntityColumn> getEntityClassColumns() {
            return entityClassColumns;
        }

        public Set<EntityColumn> getEntityClassPKColumns() {
            return entityClassPKColumns;
        }

        public Map<String, String> getAliasMap() {
            return aliasMap;
        }

        public String[] getKeyProperties() {
            if (keyProperties != null && keyProperties.size() > 0) {
                return keyProperties.toArray(new String[]{});
            }
            return new String[]{};
        }

        public void setKeyProperties(String keyProperty) {
            if (this.keyProperties == null) {
                this.keyProperties = new LinkedList<String>();
                this.keyProperties.add(keyProperty);
            } else {
                this.keyProperties.add(keyProperty);
            }
        }

        public String[] getKeyColumns() {
            if (keyColumns != null && keyColumns.size() > 0) {
                return keyColumns.toArray(new String[]{});
            }
            return new String[]{};
        }

        public void setKeyColumns(String keyColumn) {
            if (this.keyColumns == null) {
                this.keyColumns = new LinkedList<String>();
                this.keyColumns.add(keyColumn);
            } else {
                this.keyColumns.add(keyColumn);
            }
        }
    }

    /**
     * 实体字段对应数据库列的信息
     */
    public static class EntityColumn {
        private EntityTable table;
        private String property;
        private String column;
        private Class<?> javaType;
        private boolean id = false;
        private String generator;

        public EntityColumn() {
        }

        public EntityColumn(EntityTable table) {
            this.table = table;
        }

        public EntityTable getTable() {
            return table;
        }

        public void setTable(EntityTable table) {
            this.table = table;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public String getColumn() {
            return column;
        }

        public void setColumn(String column) {
            this.column = column;
        }

        public Class<?> getJavaType() {
            return javaType;
        }

        public void setJavaType(Class<?> javaType) {
            this.javaType = javaType;
        }

        public boolean isId() {
            return id;
        }

        public void setId(boolean id) {
            this.id = id;
        }

        public String getGenerator() {
            return generator;
        }

        public void setGenerator(String generator) {
            this.generator = generator;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            EntityColumn that = (EntityColumn) o;

            if (id != that.id) return false;
            if (column != null ? !column.equals(that.column) : that.column != null) return false;
            if (generator != null ? !generator.equals(that.generator) : that.generator != null) return false;
            if (javaType != null ? !javaType.equals(that.javaType) : that.javaType != null) return false;
            if (property != null ? !property.equals(that.property) : that.property != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = property != null ? property.hashCode() : 0;
            result = 31 * result + (column != null ? column.hashCode() : 0);
            result = 31 * result + (javaType != null ? javaType.hashCode() : 0);
            result = 31 * result + (id ? 1 : 0);
            result = 31 * result + (generator != null ? generator.hashCode() : 0);
            return result;
        }
    }

    /**
     * 实体类 => 表对象
     */
    private static final Map<Class<?>, EntityTable> entityTableMap = new HashMap<Class<?>, EntityTable>();

    /**
     * 获取表对象
     *
     * @param entityClass
     * @return
     */
    public static EntityTable getEntityTable(Class<?> entityClass) {
        EntityTable entityTable = entityTableMap.get(entityClass);
        if (entityTable == null) {
            initEntityNameMap(entityClass);
            entityTable = entityTableMap.get(entityClass);
        }
        if (entityTable == null) {
            throw new RuntimeException("无法获取实体类" + entityClass.getCanonicalName() + "对应的表名!");
        }
        return entityTable;
    }

    /**
     * 获取全部列
     *
     * @param entityClass
     * @return
     */
    public static Set<EntityColumn> getColumns(Class<?> entityClass) {
        return getEntityTable(entityClass).getEntityClassColumns();
    }

    /**
     * 获取主键信息
     *
     * @param entityClass
     * @return
     */
    public static Set<EntityColumn> getPKColumns(Class<?> entityClass) {
        return getEntityTable(entityClass).getEntityClassPKColumns();
    }

    /**
     * 获取查询的Select
     *
     * @param entityClass
     * @return
     */
    public static String getSelectColumns(Class<?> entityClass) {
        Set<EntityColumn> columnList = getColumns(entityClass);
        StringBuilder selectBuilder = new StringBuilder();
        boolean skipAlias = Map.class.isAssignableFrom(entityClass);
        for (EntityColumn entityColumn : columnList) {
            selectBuilder.append(entityColumn.getColumn());
            if (!skipAlias && !entityColumn.getColumn().equalsIgnoreCase(entityColumn.getProperty())) {
                selectBuilder.append(" ").append(entityColumn.getProperty().toUpperCase()).append(",");
            } else {
                selectBuilder.append(",");
            }
        }
        return selectBuilder.substring(0, selectBuilder.length() - 1);
    }

    /**
     * 获取主键的Where语句
     *
     * @param entityClass
     * @return
     */
    public static String getPrimaryKeyWhere(Class<?> entityClass) {
        Set<EntityColumn> entityColumns = getPKColumns(entityClass);
        StringBuilder whereBuilder = new StringBuilder();
        for (EntityColumn column : entityColumns) {
            whereBuilder.append(column.getColumn()).append(" = ?").append(" AND ");
        }
        return whereBuilder.substring(0, whereBuilder.length() - 4);
    }

    /**
     * 初始化实体属性
     *
     * @param entityClass
     */
    public static synchronized void initEntityNameMap(Class<?> entityClass) {
        if (entityTableMap.get(entityClass) != null) {
            return;
        }
        //表名
        EntityTable entityTable = new EntityTable();
        if (entityClass.isAnnotationPresent(Table.class)) {
            Table table = entityClass.getAnnotation(Table.class);
            if (StringUtils.isNotEmpty(table.name())) {
                entityTable.setTable(table);
            } else {
                entityTable.name = camelhumpToUnderline(entityClass.getSimpleName());
            }
        } else {
            throw new MybatisException(10011, "类[" + entityClass.getName() + "]必须要有@Table注解!");
        }

        //列
        List<Field> fieldList = getAllField(entityClass, null);
        Set<EntityColumn> columnSet = new LinkedHashSet<EntityColumn>();
        Set<EntityColumn> pkColumnSet = new LinkedHashSet<EntityColumn>();
        for (Field field : fieldList) {
            //排除字段
            if (field.isAnnotationPresent(Transient.class)) {
                continue;
            }
            EntityColumn entityColumn = new EntityColumn(entityTable);
            if (field.isAnnotationPresent(Id.class)) {
                entityColumn.setId(true);
            }

            //主键策略
            if (field.isAnnotationPresent(GeneratedValue.class)) {
                GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
                String generator = generatedValue.generator();
                if (StringUtils.isEmpty(generator)) {
                    throw new MybatisException(10011, "类[" + entityClass.getName() + "]的字段[" + field.getName()
                            + "]有@GeneratedValue注解,但是没有设置值!");
                } else {
                    entityColumn.setGenerator(generator);
                }
            }

            String columnName = null;
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                columnName = column.name();
            }
            if (StringUtils.isEmpty(columnName)) {
                columnName = camelhumpToUnderline(field.getName());
            }
            entityColumn.setProperty(field.getName());
            entityColumn.setColumn(columnName.toUpperCase());
            entityColumn.setJavaType(field.getType());

            columnSet.add(entityColumn);
            if (entityColumn.isId()) {
                pkColumnSet.add(entityColumn);
            }
        }
        entityTable.entityClassColumns = columnSet;
        if (pkColumnSet.size() == 0) {
            entityTable.entityClassPKColumns = columnSet;
        } else {
            entityTable.entityClassPKColumns = pkColumnSet;
        }

        //缓存
        entityTableMap.put(entityClass, entityTable);
    }

    /**
     * 将驼峰风格替换为下划线风格
     */
    public static String camelhumpToUnderline(String str) {
        final int size;
        final char[] chars;
        final StringBuilder sb = new StringBuilder(
                (size = (chars = str.toCharArray()).length) * 3 / 2 + 1);
        char c;
        for (int i = 0; i < size; i++) {
            c = chars[i];
            if (isUppercaseAlpha(c)) {
                sb.append('_').append(c);
            } else {
                sb.append(toUpperAscii(c));
            }
        }
        return sb.charAt(0) == '_' ? sb.substring(1) : sb.toString();
    }

    public static boolean isUppercaseAlpha(char c) {
        return (c >= 'A') && (c <= 'Z');
    }

    public static char toUpperAscii(char c) {
        if (isUppercaseAlpha(c)) {
            c -= (char) 0x20;
        }
        return c;
    }

    /**
     * 获取全部的Field
     *
     * @param entityClass
     * @param fieldList
     * @return
     */
    private static List<Field> getAllField(Class<?> entityClass, List<Field> fieldList) {
        if (fieldList == null) {
            fieldList = new LinkedList<Field>();
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
        if (superClass != null
                && !superClass.equals(Object.class)
                && (superClass.isAnnotationPresent(Entity.class)
                || (!Map.class.isAssignableFrom(superClass)
                && !Collection.class.isAssignableFrom(superClass)))) {
            return getAllField(entityClass.getSuperclass(), fieldList);
        }
        return fieldList;
    }
}
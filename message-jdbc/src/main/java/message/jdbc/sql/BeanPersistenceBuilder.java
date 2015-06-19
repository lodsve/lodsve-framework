package message.jdbc.sql;

import message.jdbc.annontations.Cache;
import message.jdbc.base.DynamicBeanUtils;
import message.utils.ObjectUtils;
import message.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 对bean的(字段,类名)(数据库列名,表名)的映射关系构建类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-10-8 上午04:25:01
 */
public class BeanPersistenceBuilder {
    private static final Logger logger = LoggerFactory.getLogger(BeanPersistenceBuilder.class);
    private BeanPersistenceDef beanPersistenceDef;        //beanPersistenceDef

    public BeanPersistenceBuilder(Class<?> clazz) {
        this.beanPersistenceDef = new BeanPersistenceDef(clazz);
    }

    public BeanPersistenceDef build() throws Exception {
        Class clazz = this.beanPersistenceDef.getClazz();
        logger.debug("build class: '{}'", clazz);

        // 类头上的注解
        Annotation[] anns = clazz.getAnnotations();
        for (int i = 0; i < anns.length; i++) {
            Annotation ann = anns[i];
            Class type = ann.annotationType();
            if (Table.class == type) {
                // 处理Table注解
                evalTable(clazz, (Table) ann);
            } else if (Cache.class == type) {
                // 处理Cache注解
                evalCache((Cache) ann);
            }
        }

        // 处理字段
        List<Field> fields = new ArrayList<Field>();
        // 获得类中所有申明的字段
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        // 处理父类
        getSubClassFields(clazz.getSuperclass(), fields);

        for (Field f : fields) {
            anns = f.getDeclaredAnnotations();
            for (Annotation ann : anns) {
                evalColumn(f, ann);
            }
        }

        buildInsertSql();
        buildUpdateSql();
        buildSelectSql();
        buildDeleteSql();

        return this.beanPersistenceDef;
    }

    private void getSubClassFields(Class<?> clazz, List<Field> fields) {
        Class<?> subClass = clazz.getSuperclass();

        if (subClass != null && !Object.class.equals(subClass)) {
            fields.addAll(Arrays.asList(subClass.getDeclaredFields()));

            getSubClassFields(subClass.getSuperclass(), fields);
        }
    }

    private void evalTable(Class<?> clazz, Table table) {
        if (logger.isDebugEnabled()) {
            logger.debug("Class is '{}' and it's annotation table '{}'", clazz.getName(), table);
        }

        String name = table.name();
        if (StringUtils.isEmpty(name)) {
            name = DynamicBeanUtils.underscoreName(clazz.getSimpleName());
        }
        // 设置表名
        this.beanPersistenceDef.setTableName(name);
    }

    private void evalCache(Cache ann) {
        if (logger.isDebugEnabled()) {
            logger.debug("annotation Cache '{}'", ann);
        }

        this.beanPersistenceDef.setCacheEnable(true);
        this.beanPersistenceDef.setCacheRegion(ann.cacheRegion());
    }

    private void buildDeleteSql() throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ");
        sql.append(this.beanPersistenceDef.getTableName());
        sql.append(" where 1 = 1 ");
        if (ObjectUtils.isNotEmpty(this.beanPersistenceDef.getIdClass())) {
            sql.append(" and ").append(this.beanPersistenceDef.getIdColumnName()).append(" = :")
                    .append(this.beanPersistenceDef.getIdFieldName());
        } else {
            logger.warn("this delete sql '{}' don't has any condition!That will delete all when this sql execute!", sql.toString());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("delete sql '{}'", sql.toString());
        }

        this.beanPersistenceDef.setDeleteSql(sql.toString());
    }

    private void buildSelectSql() throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");

        List<String> columns = new ArrayList<String>();
        if (ObjectUtils.isNotEmpty(this.beanPersistenceDef.getIdClass())) {
            columns.add(this.beanPersistenceDef.getIdColumnName());
        }

        Map<String, String> fieldColumnMapping = this.beanPersistenceDef.getFieldColumnMapping();
        for (Iterator<String> it = fieldColumnMapping.values().iterator(); it.hasNext(); ) {
            String column = it.next();
            columns.add(column);
        }

        sql.append(StringUtils.join(columns, ", "));
        sql.append(" from ").append(this.beanPersistenceDef.getTableName());

        if (ObjectUtils.isNotEmpty(this.beanPersistenceDef.getIdClass())) {
            sql.append(" where ").append(this.beanPersistenceDef.getIdColumnName()).append(" = :")
                    .append(this.beanPersistenceDef.getIdFieldName());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("select sql '{}'", sql.toString());
        }

        this.beanPersistenceDef.setSelectSql(sql.toString());
    }

    private void buildUpdateSql() throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("update ");
        sql.append(this.beanPersistenceDef.getTableName());
        sql.append(" set ");

        Map<String, String> fieldColumnMapping = this.beanPersistenceDef.getFieldColumnMapping();
        List<String> columns = new ArrayList<String>();

        for (Iterator<Map.Entry<String, String>> it = fieldColumnMapping.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, String> entry = it.next();
            String field = entry.getKey();
            String column = entry.getValue();

            columns.add(column + " = :" + field);
        }

        sql.append(StringUtils.join(columns, ", ")).append(" where 1 = 1 ");
        if (ObjectUtils.isNotEmpty(this.beanPersistenceDef.getIdClass())) {
            sql.append(" and ").append(this.beanPersistenceDef.getIdColumnName()).append(" = :")
                    .append(this.beanPersistenceDef.getIdFieldName());
        }

        this.beanPersistenceDef.setUpdateSql(sql.toString());

        if (logger.isDebugEnabled()) {
            logger.debug("update sql '{}'", sql.toString());
        }

        this.beanPersistenceDef.setUpdateSql(sql.toString());
    }

    private void buildInsertSql() throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ");
        sql.append(this.beanPersistenceDef.getTableName());

        Map<String, String> fieldColumnMapping = this.beanPersistenceDef.getFieldColumnMapping();
        List<String> columns = new ArrayList<String>();
        List<String> values = new ArrayList<String>();

        if (ObjectUtils.isNotEmpty(this.beanPersistenceDef.getIdClass())) {
            columns.add(this.beanPersistenceDef.getIdColumnName());
            values.add(":" + this.beanPersistenceDef.getIdFieldName());
        }
        for (Iterator<Map.Entry<String, String>> it = fieldColumnMapping.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, String> entry = it.next();
            String field = entry.getKey();
            String column = entry.getValue();

            columns.add(column);
            values.add(":" + field);
        }

        sql.append(" (").append(StringUtils.join(columns, ", ")).append(") values (")
                .append(StringUtils.join(values, ", ")).append(")");

        if (logger.isDebugEnabled())
            logger.debug("insert sql '{}'", sql.toString());

        this.beanPersistenceDef.setInsertSql(sql.toString());
    }

    private void evalColumn(Field field, Annotation ann) {
        if (field == null || ann == null) {
            logger.debug("field or ann is null!");
            return;
        }

        logger.debug("field '{}' annotation is '{}'", field.getName(), ann);
        Class annType = ann.annotationType();
        if (Id.class.equals(annType)) {
            // 主键
            String idFieldName = field.getName();
            String idColumnName = DynamicBeanUtils.underscoreName(idFieldName);

            this.beanPersistenceDef.setIdClass(field.getType());
            this.beanPersistenceDef.setIdFieldName(idFieldName);
            this.beanPersistenceDef.setIdColumnName(idColumnName);
        } else if (GeneratedValue.class.equals(annType)) {
            GeneratedValue gv = (GeneratedValue) ann;
            this.beanPersistenceDef.setGenerator(gv.generator());
        } else if (Column.class.equals(annType)) {
            Column column = (Column) ann;
            // 普通字段
            String fieldName = field.getName();

            String columnName = column.name();
            if (StringUtils.isEmpty(columnName)) {
                columnName = DynamicBeanUtils.underscoreName(fieldName);
            }

            this.beanPersistenceDef.addFieldColumnMapping(fieldName, columnName);
        }
    }
}

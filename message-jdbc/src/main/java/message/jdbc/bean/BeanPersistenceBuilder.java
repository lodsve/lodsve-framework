package message.jdbc.bean;

import message.jdbc.annontations.Cache;
import message.jdbc.annontations.Column;
import message.jdbc.annontations.Id;
import message.jdbc.utils.PersistentField;
import message.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.IdClass;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 对bean的(字段,类名)(数据库列名,表名)的映射关系构建类.
 *
 * @author Danny(sunhao)(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-10-8 上午04:25:01
 */
public class BeanPersistenceBuilder {
    private static final Logger logger = LoggerFactory.getLogger(BeanPersistenceBuilder.class);
    BeanPersistenceDef beanPersistenceDef;        //beanPersistenceDef
    PersistentField idField = null;                //field for id
    PersistentField nameField = null;            //field for name
    Map<String, PersistentField> mappedFields = new HashMap<String, PersistentField>();

    public BeanPersistenceBuilder(Class<?> clazz) {
        this.beanPersistenceDef = new BeanPersistenceDef(clazz);
    }

    public BeanPersistenceDef build() {
        Class clazz = this.beanPersistenceDef.getBeanClazz();
        logger.debug("build class: '{}'", clazz);

        Annotation[] anns = clazz.getAnnotations();
        for (int i = 0; i < anns.length; i++) {
            Annotation ann = anns[i];
            Class type = ann.annotationType();
            if (Entity.class == type) {
                handle((Entity) ann);
            } else if (NamedNativeQuery.class == type) {
                handle((NamedNativeQuery) ann);
            } else if (NamedNativeQueries.class == type) {
                handle((NamedNativeQueries) ann);
            } else if (IdClass.class == type) {
                handle((IdClass) ann);
            } else if (Table.class == type) {
                handle((Table) ann);
            } else if (Cache.class == type) {
                handle((Cache) ann);
            }
        }

        List<Field> fields = new ArrayList<Field>();
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        if (clazz.getSuperclass() != null && !Object.class.equals(clazz.getSuperclass())) {
            List<Field> fields_ = Arrays.asList(clazz.getSuperclass().getDeclaredFields());
            fields.addAll(fields_);
        }

        for (Field f : fields) {
            anns = f.getDeclaredAnnotations();
            for (Annotation ann : anns) {
                handle(this.beanPersistenceDef.getPersistentFieldByFieldName(f.getName()), ann);
            }
        }

        List<Method> ms = new ArrayList<Method>();
        ms.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        if (clazz.getSuperclass() != null && !Object.class.equals(clazz.getSuperclass())) {
            ms.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredMethods()));
        }

        for (Method m : ms) {
            anns = m.getDeclaredAnnotations();
            for (Annotation ann : anns) {
                handle(this.beanPersistenceDef.getPersistentFieldByFieldName(m.getName()), ann);
            }
        }

        buildCRUDSql();

        return this.beanPersistenceDef;
    }

    private void handle(IdClass ann) {
        if (logger.isDebugEnabled())
            logger.debug("annotation idClass '{}'", ann);

        this.beanPersistenceDef.setIdClass(ann.value());
    }

    private void handle(NamedNativeQueries ann) {
        if (logger.isDebugEnabled())
            logger.debug("annotation named native queries '{}'", ann);

        NamedNativeQuery[] nnqs = ann.value();
        NamedNativeQuery[] arr = nnqs;
        int len = arr.length;
        for (int i = 0; i < len; ++i) {
            NamedNativeQuery nq = arr[i];
            handle(nq);
        }
    }

    private void handle(NamedNativeQuery ann) {
        if (logger.isDebugEnabled())
            logger.debug("annotation named native query '{}'", ann);

        String name = StringUtils.trimToNull(ann.name());
        String query = StringUtils.trimToNull(ann.query());
        if ((name != null) && (query != null))
            this.beanPersistenceDef.addNamedNativeQuery(name, query);
    }

    private void handle(Entity ann) {
        if (logger.isDebugEnabled())
            logger.debug("annotation entity '{}'", ann);

        this.beanPersistenceDef.setAnnotation(true);
        String name = StringUtils.trimToNull(ann.name());
        if (name != null)
            this.beanPersistenceDef.setName(name);
    }

    private void handle(Table ann) {
        if (logger.isDebugEnabled()) {
            logger.debug("annotation table '{}'", ann);
        }

        this.beanPersistenceDef.setAnnotation(true);
        String catalog = StringUtils.trimToNull(ann.catalog());
        if (catalog != null)
            this.beanPersistenceDef.setCatalog(catalog);

        String name = StringUtils.trimToNull(ann.name());
        if (name != null)
            this.beanPersistenceDef.setName(name);

        String schema = StringUtils.trimToNull(ann.schema());
        if (schema != null)
            this.beanPersistenceDef.setSchema(schema);
    }

    private void handle(Cache ann) {
        if (logger.isDebugEnabled()) {
            logger.debug("annotation Cache '{}'", ann);
        }

        this.beanPersistenceDef.setCacheEnable(true);
        this.beanPersistenceDef.setCacheRegion(ann.cacheRegion());
    }

    private void buildCRUDSql() {
        logger.debug("begin build crud sql!");
        for (Iterator it = this.beanPersistenceDef.getMappedFields().values().iterator(); it.hasNext(); ) {
            PersistentField pf = (PersistentField) it.next();
            if (this.beanPersistenceDef.isAnnotation()) {
                if (pf.isAnnotation()) {
                    this.mappedFields.put(pf.getFieldName(), pf);
                    if (pf.isIdField()) {
                        this.idField = pf;
                    }
                    if (pf.isNameField()) {
                        this.nameField = pf;
                    }
                }
            } else {
                this.mappedFields.put(pf.getFieldName(), pf);
                if (StringUtils.trimToEmpty(this.beanPersistenceDef.getIdFieldName()).equalsIgnoreCase(pf.getFieldName())) {
                    this.idField = pf;
                }
            }
        }

        buildInsertSql();
        buildUpdateSql();
        buildSelectSql();
        buildSelectOneSql();
        buildDeleteSql();
    }

    private void buildDeleteSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ");
        sql.append(this.beanPersistenceDef.getName());
        sql.append(" where ");

        if (this.idField != null) {
            sql.append(this.idField.getColumnName());
            sql.append("=:");
            sql.append(this.idField.getFieldName());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("delete sql '{}'", sql.toString());
        }

        this.beanPersistenceDef.setDeleteSql(sql.toString());
    }

    private void buildSelectOneSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");

        int i = 0;
        for (Iterator it = this.mappedFields.values().iterator(); it.hasNext(); ) {
            PersistentField pf = (PersistentField) it.next();
            if (i > 0)
                sql.append(",");

            sql.append(" ");
            sql.append(pf.getColumnName());
            sql.append(" as ");
            sql.append(pf.getFieldName());
            ++i;
        }
        sql.append(" from ");
        sql.append(this.beanPersistenceDef.getName());
        sql.append(" where ");
        sql.append(this.idField.getColumnName());
        sql.append("=? ");

        if (logger.isDebugEnabled()) {
            logger.debug("select one sql '{}'", sql.toString());
        }

        this.beanPersistenceDef.setSelectOneSql(sql.toString());
    }

    private void buildSelectSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");

        int i = 0;
        for (Iterator it = this.mappedFields.values().iterator(); it.hasNext(); ) {
            PersistentField pf = (PersistentField) it.next();
            if (i > 0)
                sql.append(",");

            sql.append(" ");
            sql.append(pf.getColumnName());
            sql.append(" as ");
            sql.append(pf.getFieldName());
            ++i;
        }
        sql.append(" from ");
        sql.append(this.beanPersistenceDef.getName());
        sql.append(" ");

        if (logger.isDebugEnabled()) {
            logger.debug("select sql '{}'", sql.toString());
        }

        this.beanPersistenceDef.setSelectSql(sql.toString());
    }

    private void buildUpdateSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("update ");
        sql.append(this.beanPersistenceDef.getName());
        sql.append(" set ");

        int i = 0;
        for (Iterator it = this.mappedFields.values().iterator(); it.hasNext(); ) {
            PersistentField pf = (PersistentField) it.next();
            if (i > 0)
                sql.append(",");

            sql.append(pf.getColumnName());
            sql.append("=:");
            sql.append(pf.getFieldName());
            ++i;
        }
        sql.append(" where ");

        if (this.idField != null) {
            sql.append(this.idField.getColumnName());
            sql.append("=:");
            sql.append(this.idField.getFieldName());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("update sql '{}'", sql.toString());
        }

        this.beanPersistenceDef.setUpdateSql(sql.toString());
    }

    private void buildInsertSql() {
        PersistentField pf;
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ");
        sql.append(this.beanPersistenceDef.getName());
        sql.append(" (");
        int i = 0;
        for (Iterator it = this.mappedFields.values().iterator(); it.hasNext(); ) {
            pf = (PersistentField) it.next();
            if (i > 0)
                sql.append(",");

            sql.append(pf.getColumnName());
            ++i;
        }
        sql.append(" ) values (");
        i = 0;
        for (Iterator it = this.mappedFields.values().iterator(); it.hasNext(); ) {
            pf = (PersistentField) it.next();
            if (i > 0)
                sql.append(",");

            sql.append(":");
            sql.append(pf.getFieldName());
            ++i;
        }
        sql.append(" )");

        if (logger.isDebugEnabled())
            logger.debug("insert sql '{}'", sql.toString());

        this.beanPersistenceDef.setInsertSql(sql.toString());
    }

    private void handle(PersistentField pf, Annotation ann) {
        if (pf == null || ann == null) {
            logger.debug("persistentField or ann is null!");
            return;
        }

        logger.debug("field '{}' annotation is '{}'", pf.getFieldName(), ann);
        pf.setAnnotation(true);
        Class type = ann.annotationType();
        if (type == Id.class) {
            this.beanPersistenceDef.setIdFieldName(pf.getFieldName());
            pf.setIdField(true);
            this.beanPersistenceDef.setIdClass(pf.getJavaType());
        } else if (type == GeneratedValue.class) {
            GeneratedValue gv = (GeneratedValue) ann;
            this.beanPersistenceDef.setGenerator(gv.generator());
        } else if (type == Column.class) {
            Column col = (Column) ann;
            String columnName = col.name().trim();
            if (StringUtils.isNotEmpty(columnName)) {
                pf.setColumnName(columnName);
            }
            pf.setLength(col.length());
            pf.setNullable(col.nullable());
            pf.setUnique(col.unique());
        }
    }
}

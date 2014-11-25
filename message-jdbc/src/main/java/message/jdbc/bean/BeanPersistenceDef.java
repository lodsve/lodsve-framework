package message.jdbc.bean;

import message.jdbc.dynamic.DynamicBeanUtils;
import message.jdbc.utils.PersistentField;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * bean的(字段,类名)(数据库列名,表名)的映射关系.
 *
 * @author Danny(sunhao)(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-10-8 上午04:19:21
 */
public class BeanPersistenceDef {
    private static final String DEFAULT_ID_NAME = "id";
    private Class<?> clazz;                                                            //bean的class
    private Class<?> idClass;                                                        //定义为主键的那个字段的类型
    private Map<String, String> namedNativeQueries = new HashMap();
    private String catalog;
    private String name;                                                            //数据库表名
    private String schema;
    private String generator;                                                        //数据库sequence名(针对oracle)
    private String idFieldName;                                                        //主键数据库列名
    private boolean annotation;                                                        //是否是基于注解的
    private String insertSql;                                                        //向数据库插入对象的sql
    private String updateSql;                                                        //更新对象的sql
    private String deleteSql;                                                        //删除对象的sql
    private String selectSql;                                                        //查询对象的sql
    private String selectOneSql;                                                    //查询单个对象的sql
    private boolean cacheEnable = false;                                            //是否存在缓存中
    private String cacheRegion = "ENTITY_REGION";                                   //存在缓存中的范围
    private Map<String, PersistentField> mappedFields = new HashMap();

    private BeanPersistenceDef() {
    }

    public BeanPersistenceDef(Class<?> clazz) {
        this.clazz = clazz;
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(clazz);
        for (int i = 0; i < propertyDescriptors.length; ++i) {
            PropertyDescriptor pd = propertyDescriptors[i];
            Method writeMethod = pd.getWriteMethod();
            Method readMethod = pd.getReadMethod();
            if (writeMethod != null) {
                if (readMethod == null)
                    return;

                String fname = pd.getName();
                PersistentField pf = new PersistentField();
                pf.setFieldName(fname);
                pf.setJavaType(readMethod.getReturnType());
                pf.setWriteName(writeMethod.getName());
                pf.setReadName(readMethod.getName());

                this.mappedFields.put(fname.toLowerCase(), pf);
                String underscoredName = DynamicBeanUtils.underscoreName(fname);
                if (!(fname.toLowerCase().equals(underscoredName)))
                    pf.setColumnName(underscoredName);
                else
                    pf.setColumnName(fname.toLowerCase());

                if ("id".equalsIgnoreCase(fname)) {
                    this.idFieldName = fname;
                    this.idClass = readMethod.getReturnType();
                }
            }
        }
        this.name = DynamicBeanUtils.underscoreName(ClassUtils.getShortName(this.clazz));
    }

    public Class<?> getBeanClazz() {
        return this.clazz;
    }

    public PersistentField getPersistentFieldByMethodName(String name) {
        PersistentField result = null;
        for (Iterator it = this.mappedFields.values().iterator(); it.hasNext(); ) {
            PersistentField pf = (PersistentField) it.next();
            if ((name.equalsIgnoreCase(pf.getReadName())) || (name.equalsIgnoreCase(pf.getWriteName()))) {
                result = pf;
                break;
            }
        }
        return result;
    }

    public PersistentField getPersistentFieldByFieldName(String name) {
        PersistentField result = null;
        for (Iterator it = this.mappedFields.values().iterator(); it.hasNext(); ) {
            PersistentField pf = (PersistentField) it.next();
            if (name.equalsIgnoreCase(pf.getFieldName())) {
                result = pf;
                break;
            }
        }
        return result;
    }

    public PersistentField getIdPersistentField() {
        PersistentField result = null;
        for (Iterator it = this.mappedFields.values().iterator(); it.hasNext(); ) {
            PersistentField pf = (PersistentField) it.next();
            if (pf.isIdField()) {
                result = pf;
                break;
            }
        }
        return result;
    }

    public PersistentField getNamePersistentField() {
        PersistentField result = null;
        for (Iterator it = this.mappedFields.values().iterator(); it.hasNext(); ) {
            PersistentField pf = (PersistentField) it.next();
            if (pf.isNameField()) {
                result = pf;
                break;
            }
        }
        return result;
    }

    public void addNamedNativeQuery(String name, String query) {
        this.namedNativeQueries.put(name, query);
    }

    public Class<?> getIdClass() {
        return this.idClass;
    }

    public void setIdClass(Class<?> idClass) {
        this.idClass = idClass;
    }

    public String getCatalog() {
        return this.catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchema() {
        return this.schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getGenerator() {
        return this.generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getIdFieldName() {
        return this.idFieldName;
    }

    public void setIdFieldName(String idFieldName) {
        this.idFieldName = idFieldName;
    }

    public boolean isAnnotation() {
        return this.annotation;
    }

    public void setAnnotation(boolean annotation) {
        this.annotation = annotation;
    }

    public Map<String, String> getNamedNativeQueries() {
        return this.namedNativeQueries;
    }

    public void setNamedNativeQueries(Map<String, String> namedNativeQueries) {
        this.namedNativeQueries = namedNativeQueries;
    }

    public Map<String, PersistentField> getMappedFields() {
        return this.mappedFields;
    }

    public void setMappedFields(Map<String, PersistentField> mappedFields) {
        this.mappedFields = mappedFields;
    }

    public String getInsertSql() {
        return this.insertSql;
    }

    public void setInsertSql(String insertSql) {
        this.insertSql = insertSql;
    }

    public String getUpdateSql() {
        return this.updateSql;
    }

    public void setUpdateSql(String updateSql) {
        this.updateSql = updateSql;
    }

    public String getDeleteSql() {
        return this.deleteSql;
    }

    public void setDeleteSql(String deleteSql) {
        this.deleteSql = deleteSql;
    }

    public String getSelectSql() {
        return this.selectSql;
    }

    public void setSelectSql(String selectSql) {
        this.selectSql = selectSql;
    }

    public boolean isCacheEnable() {
        return this.cacheEnable;
    }

    public void setCacheEnable(boolean cacheEnable) {
        this.cacheEnable = cacheEnable;
    }

    public String getSelectOneSql() {
        return this.selectOneSql;
    }

    public void setSelectOneSql(String selectOneSql) {
        this.selectOneSql = selectOneSql;
    }

    public String getCacheRegion() {
        return cacheRegion;
    }

    public void setCacheRegion(String cacheRegion) {
        this.cacheRegion = cacheRegion;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }
}

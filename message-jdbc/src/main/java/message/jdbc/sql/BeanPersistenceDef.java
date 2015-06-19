package message.jdbc.sql;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.HashMap;
import java.util.Map;

/**
 * bean的(字段,类名)(数据库列名,表名)的映射关系.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-10-8 上午04:19:21
 */
public class BeanPersistenceDef {
    /**
     * 对应的类
     */
    private Class<?> clazz;
    /**
     * 表名
     */
    private String tableName;
    /**
     * id字段的类型
     */
    private Class<?> idClass;
    /**
     * id字段在对象中的字段名称
     */
    private String idFieldName;
    /**
     * id字段在数据库中的列名
     */
    private String idColumnName;
    /**
     * 序列名
     */
    private String generator;
    /**
     * 类中的字段名与数据库列名的映射关系,不包含主键字段
     */
    private Map<String, String> fieldColumnMapping = new HashMap<String, String>();
    /**
     * 向数据库插入对象的sql
     */
    private String insertSql;
    /**
     * 更新对象的sql
     */
    private String updateSql;
    /**
     * 删除对象的sql
     */
    private String deleteSql;
    /**
     * 查询对象的sql
     */
    private String selectSql;
    /**
     * 是否启用缓存
     */
    private boolean cacheEnable = false;
    /**
     * 缓存默认的域名
     */
    private String cacheRegion = "ENTITY_REGION";

    public BeanPersistenceDef(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Class<?> getIdClass() {
        return idClass;
    }

    public void setIdClass(Class<?> idClass) {
        this.idClass = idClass;
    }

    public String getIdFieldName() {
        return idFieldName;
    }

    public void setIdFieldName(String idFieldName) {
        this.idFieldName = idFieldName;
    }

    public String getIdColumnName() {
        return idColumnName;
    }

    public void setIdColumnName(String idColumnName) {
        this.idColumnName = idColumnName;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public Map<String, String> getFieldColumnMapping() {
        return fieldColumnMapping;
    }

    public void setFieldColumnMapping(Map<String, String> fieldColumnMapping) {
        this.fieldColumnMapping = fieldColumnMapping;
    }

    public String getInsertSql() {
        return insertSql;
    }

    public void setInsertSql(String insertSql) {
        this.insertSql = insertSql;
    }

    public String getUpdateSql() {
        return updateSql;
    }

    public void setUpdateSql(String updateSql) {
        this.updateSql = updateSql;
    }

    public String getDeleteSql() {
        return deleteSql;
    }

    public void setDeleteSql(String deleteSql) {
        this.deleteSql = deleteSql;
    }

    public String getSelectSql() {
        return selectSql;
    }

    public void setSelectSql(String selectSql) {
        this.selectSql = selectSql;
    }

    public boolean isCacheEnable() {
        return cacheEnable;
    }

    public void setCacheEnable(boolean cacheEnable) {
        this.cacheEnable = cacheEnable;
    }

    public String getCacheRegion() {
        return cacheRegion;
    }

    public void setCacheRegion(String cacheRegion) {
        this.cacheRegion = cacheRegion;
    }

    public void addFieldColumnMapping(String field, String column) {
        this.fieldColumnMapping.put(field, column);
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }
}

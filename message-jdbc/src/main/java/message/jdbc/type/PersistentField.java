package message.jdbc.type;

import java.io.Serializable;

/**
 * save bean class fields
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2012-4-10 上午02:00:37
 */
public class PersistentField implements Serializable {
    private static final long serialVersionUID = -2932429421268936487L;

    /**
     * 字段名
     */
    private String fieldName;
    /**
     * 该字段Java类型
     */
    private Class javaType;
    /**
     * 该字段对应数据库表的sql类型
     */
    private int sqlType;
    /**
     * setter方法名
     */
    private String writeName;
    /**
     * 短名?
     */
    private String shortName;
    /**
     * 类名
     */
    private String beanName;
    /**
     * getter方法名
     */
    private String readName;
    /**
     * column name in database
     */
    private String columnName;
    /**
     * length in database
     */
    private int length;
    /**
     * can be null
     */
    private boolean nullable;
    /**
     * only one
     */
    private boolean unique;
    /**
     * is annotation
     */
    private boolean annotation;
    /**
     * id field
     */
    private boolean idField;
    private boolean nameField;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Class getJavaType() {
        return javaType;
    }

    public void setJavaType(Class javaType) {
        this.javaType = javaType;
    }

    public int getSqlType() {
        return sqlType;
    }

    public void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }

    public String getWriteName() {
        return writeName;
    }

    public void setWriteName(String writeName) {
        this.writeName = writeName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getReadName() {
        return readName;
    }

    public void setReadName(String readName) {
        this.readName = readName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isAnnotation() {
        return annotation;
    }

    public void setAnnotation(boolean annotation) {
        this.annotation = annotation;
    }

    public boolean isIdField() {
        return idField;
    }

    public void setIdField(boolean idField) {
        this.idField = idField;
    }

    public boolean isNameField() {
        return nameField;
    }

    public void setNameField(boolean nameField) {
        this.nameField = nameField;
    }
}

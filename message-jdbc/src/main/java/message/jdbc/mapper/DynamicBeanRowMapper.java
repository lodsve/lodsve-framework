package message.jdbc.mapper;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import message.base.convert.ConvertGetter;
import message.jdbc.base.DynamicBeanUtils;
import message.jdbc.type.PersistentField;
import message.jdbc.helper.SqlHelper;
import message.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态创建rowMapper
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2012-4-10 上午12:37:12
 */
public class DynamicBeanRowMapper extends ColumnMapRowMapper {
    private static final Logger logger = LoggerFactory.getLogger(DynamicBeanRowMapper.class);

    private Constructor constructor;
    private Map mappedFields;
    private String sql;
    private String mapperKey;
    private final static Map mappers = new HashMap();

    public DynamicBeanRowMapper() {

    }

    public DynamicBeanRowMapper(Class clazz) {
        super.setClazz(clazz);
    }

    public static RowMapper getInstance(Class clazz, SqlHelper sqlHelper, String sql) {
        String key = DynamicBeanUtils.createMapperKey(clazz, sql);
        RowMapper rm = (RowMapper) mappers.get(key);

        if (rm != null)
            return rm;

        DynamicBeanRowMapper mapper = new DynamicBeanRowMapper(clazz);
        mapper.setClazz(clazz);
        mapper.setSqlHelper(sqlHelper);
        mapper.setSql(sql);
        mapper.setMapperKey(key);
        mapper.initialize();

        return mapper;
    }

    protected void initialize() {
        try {
            this.constructor = super.getClazz().getConstructor((Class[]) null);
        } catch (Exception e) {
            throw new DataAccessResourceFailureException("there is no default constructor in class " + super.getClazz().getName());
        }

        this.mappedFields = new HashMap();
        Class metaClass = super.getClazz();

        if (metaClass != null) {
            PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(metaClass);
            for (int i = 0; i < pds.length; i++) {
                PropertyDescriptor pd = pds[i];
                //获得应该用于写入属性值的方法(setter)
                Method writeMethod = pd.getWriteMethod();
                //获得应该用于读取属性值的方法(getter)
                Method readMethod = pd.getReadMethod();

                if (writeMethod == null || readMethod == null)
                    //如果都为空，跳出本次循环
                    continue;

                //字段名
                String fieldName = pd.getName();
                PersistentField field = new PersistentField();
                field.setFieldName(fieldName);
                field.setJavaType(readMethod.getReturnType());
                field.setWriteName(writeMethod.getName());

                String underscoredName = StringUtils.EMPTY;
                Field f = null;
                try {
                    f = metaClass.getDeclaredField(fieldName);
                } catch (Exception e) {
                    logger.debug("this field named '{}' has not exist!", fieldName);
                    //转成数据库中的字段格式(约定好的)
                    underscoredName = DynamicBeanUtils.underscoreName(fieldName);
                }

                if (f != null) {
                    //转成数据库中的字段格式(约定好的)
                    underscoredName = DynamicBeanUtils.underscoreName(fieldName);
                }

                this.mappedFields.put(fieldName.toLowerCase(), field);

                if (!fieldName.toLowerCase().equals(underscoredName)) {
                    this.mappedFields.put(underscoredName, field);
                }
            }
        }
    }

    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        DynamicRowMapper dynamicRowMapper = (DynamicRowMapper) this.mappers.get(this.mapperKey);

        if (dynamicRowMapper != null)
            return dynamicRowMapper.mapRow(rs, rowNum);

        StringBuffer script = new StringBuffer();
        if (this.getClazz() == null)
            throw new InvalidDataAccessResourceUsageException("not found!");

        Object result = null;

        try {
            result = this.constructor.newInstance((Object[]) null);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            String column = JdbcUtils.lookupColumnName(metaData, i).toLowerCase();
            PersistentField field = (PersistentField) this.mappedFields.get(column);

            if (field == null) continue;

            BeanWrapperImpl wrapper = new BeanWrapperImpl(getClazz());
            wrapper.setWrappedInstance(result);

            int type = metaData.getColumnType(i);
            field.setSqlType(type);

            Object value;
            Class fieldType = field.getJavaType();
            if (fieldType.equals(String.class)) {
                if (type == Types.LONGVARCHAR) {
                    this.addFieldContent(script, field, "sqlHelper.getLongStringValue($1,", i, ")");
                    value = this.getLongStringValue(rs, i);
                } else if (type == Types.CLOB) {
                    this.addFieldContent(script, field, "sqlHelper.getClobStringValue($1,", i, ")");
                    value = this.getClobStringValue(rs, i);
                } else {
                    this.addFieldContent(script, field, "$1.getString(", i, ")");
                    value = rs.getString(i);
                }
            } else if (fieldType.equals(byte.class)) {
                addFieldContent(script, field, "$1.getByte(", i, ")");
                value = new Byte(rs.getByte(i));
            } else if (fieldType.equals(Byte.class)) {
                addFieldContent(script, field, "new Byte($1.getByte(", i, "))");
                value = new Byte(rs.getByte(i));
            } else if (fieldType.equals(short.class)) {
                addFieldContent(script, field, "$1.getShort(", i, ")");
                value = new Short(rs.getShort(i));
            } else if (fieldType.equals(Short.class)) {
                addFieldContent(script, field, "new Short($1.getShort(", i, "))");
                value = new Short(rs.getShort(i));
            } else if (fieldType.equals(int.class)) {
                addFieldContent(script, field, "$1.getInt(", i, ")");
                value = new Integer(rs.getInt(i));
            } else if (fieldType.equals(Integer.class)) {
                addFieldContent(script, field, "new Integer($1.getInt(", i, "))");
                value = new Integer(rs.getInt(i));
            } else if (fieldType.equals(long.class)) {
                addFieldContent(script, field, "$1.getLong(", i, ")");
                value = new Long(rs.getLong(i));
            } else if (fieldType.equals(Long.class)) {
                addFieldContent(script, field, "new Long($1.getLong(", i, "))");
                value = new Long(rs.getLong(i));
            } else if (fieldType.equals(float.class)) {
                addFieldContent(script, field, "$1.getFloat(", i, ")");
                value = new Float(rs.getFloat(i));
            } else if (fieldType.equals(Float.class)) {
                addFieldContent(script, field, "new Float($1.getFloat(", i, "))");
                value = new Float(rs.getFloat(i));
            } else if (fieldType.equals(double.class)) {
                addFieldContent(script, field, "$1.getDouble(", i, ")");
                value = new Double(rs.getDouble(i));
            } else if (fieldType.equals(Double.class)) {
                addFieldContent(script, field, "new Double($1.getDouble(", i, "))");
                value = new Double(rs.getDouble(i));
            } else if (fieldType.equals(BigDecimal.class)) {
                addFieldContent(script, field, "$1.getBigDecimal(", i, ")");
                value = rs.getBigDecimal(i);
            } else if (fieldType.equals(boolean.class)) {
                addFieldContent(script, field, "$1.getBoolean(", i, ")");
                value = (rs.getBoolean(i)) ? Boolean.TRUE : Boolean.FALSE;
            } else if (fieldType.equals(Boolean.class)) {
                addFieldContent(script, field, "new Boolean($1.getBoolean(", i, "))");
                value = (rs.getBoolean(i)) ? Boolean.TRUE : Boolean.FALSE;
            } else if (contantInConvert(fieldType)) {
                String val = rs.getString(i);

                script.append("bean.");
                script.append(field.getWriteName());
                script.append("(");
                String methodName = ((val == null) ? "getPoJoNullValue" : "getPoJoValue");
                script.append("(").append(fieldType.getName()).append(")sqlHelper.getConvert(")
                        .append(fieldType.getName()).append(".class).")
                        .append(methodName).append("($1.getString(");
                script.append(i);
                script.append(")));\n");

                if (StringUtils.isEmpty(val)) {
                    value = this.getSqlHelper().getConvert(fieldType).getPoJoNullValue(val);
                } else {
                    value = this.getSqlHelper().getConvert(fieldType).getPoJoValue(val);
                }
            } else {
                addFieldContent(script, field, "(" + field.getJavaType().getName()
                        + ")org.springframework.jdbc.support.JdbcUtils.getResultSetValue($1,", i, ")");
                value = JdbcUtils.getResultSetValue(rs, i);
            }

            if (value != null) {
                if (wrapper.isWritableProperty(field.getFieldName())) {
                    try {
                        wrapper.setPropertyValue(field.getFieldName(), value);
                    } catch (NotWritablePropertyException ex) {
                        throw new DataRetrievalFailureException("Unable to map column " + column + " to property "
                                + field.getFieldName(), ex);
                    }
                } else {
                    if (rowNum == 0) {
                        logger.warn("Unable to access the setter for " + field.getFieldName() + ".  Check that "
                                + "set" + StringUtils.capitalize(field.getFieldName())
                                + " is declared and has public access.");
                    }
                }
            }
        }

        this.createDynamicMapper(script.toString());

        return result;
    }

    private void createDynamicMapper(String string) {
        StringBuffer script = new StringBuffer();
        script.append(super.getClazz().getName());
        script.append(" bean = new ");
        script.append(this.getClazz().getName());
        script.append("();\n");
        script.append(string);

        String scriptContent = script.toString();
        try {
            ClassPool cp = DynamicBeanUtils.classPool;
            //创建一个类
            CtClass ctClass = cp.makeClass("message.jdbc.dynamic.DynamicBeanRowMapperImpl$" + System.currentTimeMillis());

            //创建一个接口(DynamicRowMapper)
            CtClass ctInterface = cp.makeInterface("message.jdbc.mapper.DynamicRowMapper");
            //上面创建的类实现上面的接口
            ctClass.addInterface(ctInterface);

            //创建一个字段(SqlHelper)
            CtField sqlHelperField = CtField.make("private message.jdbc.helper.SqlHelper sqlHelper;", ctClass);
            //加入这个字段
            ctClass.addField(sqlHelperField);

            //创建一个setter方法
            CtMethod setterMethod = CtMethod.make("" +
                    "public void setSqlHelper(message.jdbc.helper.SqlHelper sh){" +
                    "	this.sqlHelper = sh;" +
                    "}", ctClass);
            //加入这个方法
            ctClass.addMethod(setterMethod);

            //创建一个getter方法
            CtMethod getterMethod = CtMethod.make("" +
                    "public message.jdbc.helper.SqlHelper getSqlHelper(){" +
                    "	return this.sqlHelper;" +
                    "}", ctClass);
            //加入这个方法
            ctClass.addMethod(getterMethod);

            //创建实现RowMapper接口的mapRow方法
            CtMethod mapRowMethod = CtMethod.make("" +
                    "public Object mapRow(java.sql.ResultSet rs, int i) throws java.sql.SQLException{" +
                    "	return null;" +
                    "}", ctClass);
            //方法体
            StringBuffer body = new StringBuffer();
            body.append("{\n");
            body.append(scriptContent);
            body.append("return bean;");
            body.append("}\n");
            mapRowMethod.setBody(body.toString());
            //加入这个方法
            ctClass.addMethod(mapRowMethod);

            DynamicRowMapper obj = (DynamicRowMapper) ctClass.toClass().newInstance();
            obj.setSqlHelper(this.getSqlHelper());

            this.mappers.put(this.mapperKey, obj);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void addFieldContent(StringBuffer script, PersistentField fieldMeta, String methodName, int i, String endStr) {
        script.append("bean.");
        script.append(fieldMeta.getWriteName());
        script.append("(");
        script.append(methodName);
        script.append(i);
        script.append(endStr);
        script.append(");\n");
    }

    private boolean contantInConvert(Class fieldType) {
        return ConvertGetter.class.isAssignableFrom(fieldType) && this.getSqlHelper().getConvert(fieldType) != null;
    }

    public Constructor getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor constructor) {
        this.constructor = constructor;
    }

    public Map getMappedFields() {
        return mappedFields;
    }

    public void setMappedFields(Map mappedFields) {
        this.mappedFields = mappedFields;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getMapperKey() {
        return mapperKey;
    }

    public void setMapperKey(String mapperKey) {
        this.mapperKey = mapperKey;
    }

    public Map getMappers() {
        return mappers;
    }
}

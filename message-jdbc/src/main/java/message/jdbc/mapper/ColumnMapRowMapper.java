package message.jdbc.mapper;

import message.jdbc.base.DynamicBeanUtils;
import message.jdbc.helper.SqlHelper;
import message.jdbc.sql.BeanPersistenceDef;
import message.jdbc.sql.BeanPersistenceHelper;
import message.utils.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;

import javax.persistence.Table;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;
import java.util.Map;

/**
 * row mapper
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2012-4-10 上午12:37:24
 */
public class ColumnMapRowMapper implements RowMapper {

    private SqlHelper sqlHelper;
    private Class<?> clazz;

    public ColumnMapRowMapper() {
    }

    public ColumnMapRowMapper(SqlHelper sqlHelper, Class<?> clazz) {
        this.sqlHelper = sqlHelper;
        this.clazz = clazz;
    }

    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        /**
         * ResultSetMetaData : 可用于获取关于 ResultSet 对象中列的类型和属性信息的对象
         * getMetaDate() : 获取此 ResultSet 对象的列的编号、类型和属性
         */
        ResultSetMetaData metaData = rs.getMetaData();
        //获取查询得到的行数
        int columnCount = metaData.getColumnCount();
        Map valueMap = this.createColumnMap(columnCount);

        for (int i = 0; i < columnCount; i++) {
            //获取每一列的标题
            String columnName = JdbcUtils.lookupColumnName(metaData, i);
            //数据库的字段名转成类的字段名(pk_id --> pkId)
            String fieldName = findFieldNameByColumnName(columnName);
            //获取指定列的 SQL 类型
            int type = metaData.getColumnType(i);

            Object obj;
            if (type == Types.CHAR || type == Types.VARCHAR) {
                //字符类型的
                obj = this.getStringValue(rs, i);
            } else if (type == Types.LONGVARCHAR) {
                //long
                obj = this.getLongAsString(rs, i);
            } else if (type == Types.CLOB) {
                //clob
                try {
                    obj = sqlHelper.getClobAsString(rs, i);
                } catch (IOException e) {
                    obj = "";
                }
            } else {
                //other
                obj = this.getColumnValue(rs, i);
            }

            valueMap.put(columnName, obj);
            valueMap.put(fieldName, obj);
        }

        return valueMap;
    }

    protected Map createColumnMap(int columnCount) {
        return new LinkedCaseInsensitiveMap(columnCount);
    }

    protected String getStringValue(ResultSet rs, int column) throws SQLException {
        return rs.getString(column);
    }

    protected String getLongAsString(ResultSet rs, int column) throws SQLException {
        return sqlHelper.getLongAsString(rs, column);
    }

    protected String getClobStringValue(ResultSet rs, int column) throws SQLException {
        try {
            return sqlHelper.getClobAsString(rs, column);
        } catch (IOException e) {
            return StringUtils.EMPTY;
        }
    }

    protected Object getColumnValue(ResultSet rs, int column) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, column);
    }

    public SqlHelper getSqlHelper() {
        return sqlHelper;
    }

    public void setSqlHelper(SqlHelper sqlHelper) {
        this.sqlHelper = sqlHelper;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String findFieldNameByColumnName(String columnName) {
        if (StringUtils.isEmpty(columnName)) {
            return StringUtils.EMPTY;
        }

        if (!this.clazz.isAnnotationPresent(Table.class)) {
            return DynamicBeanUtils.decodeUnderscoreName(columnName);
        }

        try {
            BeanPersistenceDef beanPersistenceDef = BeanPersistenceHelper.getBeanPersistenceDef(clazz);
            if (columnName.equals(beanPersistenceDef.getIdColumnName())) {
                return beanPersistenceDef.getIdFieldName();
            }

            Map<String, String> maps = beanPersistenceDef.getFieldColumnMapping();
            Iterator<Map.Entry<String, String>> it = maps.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String fieldName = entry.getKey();
                String _columnName = entry.getValue();

                if (columnName.equals(_columnName)) {
                    return fieldName;
                }
            }

            return StringUtils.EMPTY;
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

    public String findColumnNameByFieldName(String fieldName) {
        if (StringUtils.isEmpty(fieldName)) {
            return StringUtils.EMPTY;
        }

        if (!this.clazz.isAnnotationPresent(Table.class)) {
            return DynamicBeanUtils.underscoreName(fieldName);
        }

        try {
            BeanPersistenceDef beanPersistenceDef = BeanPersistenceHelper.getBeanPersistenceDef(clazz);
            if (fieldName.equals(beanPersistenceDef.getIdFieldName())) {
                return beanPersistenceDef.getIdColumnName();
            }

            Map<String, String> maps = beanPersistenceDef.getFieldColumnMapping();
            Iterator<Map.Entry<String, String>> it = maps.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String _fieldName = entry.getKey();
                String columnName = entry.getValue();

                if (fieldName.equals(_fieldName)) {
                    return columnName;
                }
            }

            return StringUtils.EMPTY;
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }
}

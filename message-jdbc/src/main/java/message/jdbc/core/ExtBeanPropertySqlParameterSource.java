package message.jdbc.core;

import message.datasource.helper.SqlHelper;
import message.jdbc.convert.Convert;
import message.jdbc.convert.ConvertGetter;
import message.jdbc.convert.ConvertHelper;
import message.jdbc.type.ClobStringSqlTypeValue;
import message.jdbc.type.LongStringSqlTypeValue;
import message.jdbc.type.ParamType;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-10-8 上午05:20:39
 */
public class ExtBeanPropertySqlParameterSource extends AbstractSqlParameterSource {
    private final BeanWrapper beanWrapper;
    private String[] propertyNames = null;
    private SqlHelper sqlHelper;

    public ExtBeanPropertySqlParameterSource(Object object, SqlHelper sqlHelper) {
        this.beanWrapper = new BeanWrapperImpl(object);
        this.sqlHelper = sqlHelper;
    }

    public Object getValue(String paramName) throws IllegalArgumentException {
        ParamType paramType;
        try {
            paramType = ParamType.getParamType(paramName);
            if (paramType.getValueType() == 1)
                return new ClobStringSqlTypeValue(this.sqlHelper, (String) this.beanWrapper.getPropertyValue(paramType.getRealParamName()));

            if (paramType.getValueType() == 2) {
                return new LongStringSqlTypeValue(this.sqlHelper, (String) this.beanWrapper.getPropertyValue(paramType.getRealParamName()));
            }

            Convert convert = getConvert(paramName);
            if (convert != null) {
                ConvertGetter convertGetter = (ConvertGetter) this.beanWrapper.getPropertyValue(paramType.getRealParamName());
                if (convertGetter == null) {
                    return convert.getDbNullValue(convertGetter);
                } else {
                    return convert.getDbValue(convertGetter);
                }
            }

            return this.beanWrapper.getPropertyValue(paramType.getRealParamName());
        } catch (NotReadablePropertyException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    private Convert getConvert(String paramName) {
        try {
            Field f = this.beanWrapper.getWrappedClass().getDeclaredField(paramName);
            Class<?> clazz = f.getType();
            if (ConvertGetter.class.isAssignableFrom(clazz)) {
                return ConvertHelper.getConvert((Class<? extends ConvertGetter>) f.getType());
            } else {
                return null;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String[] getReadablePropertyNames() {
        synchronized (this) {
            if (this.propertyNames == null) {
                List names = new ArrayList();
                PropertyDescriptor[] props = this.beanWrapper.getPropertyDescriptors();
                for (int i = 0; i < props.length; ++i)
                    if (this.beanWrapper.isReadableProperty(props[i].getName()))
                        names.add(props[i].getName());

                this.propertyNames = ((String[]) (String[]) names.toArray(new String[names.size()]));
            }
        }

        return this.propertyNames;
    }

    public boolean hasValue(String paramName) {
        ParamType paramType = ParamType.getParamType(paramName);
        return this.beanWrapper.isReadableProperty(paramType.getRealParamName());
    }

    public SqlHelper getSqlHelper() {
        return sqlHelper;
    }

    public void setSqlHelper(SqlHelper sqlHelper) {
        this.sqlHelper = sqlHelper;
    }

}

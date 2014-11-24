package message.jdbc.ext;

import message.jdbc.utils.ClobStringSqlTypeValue;
import message.jdbc.utils.LongStringSqlTypeValue;
import message.jdbc.utils.ParamType;
import message.jdbc.utils.helper.SqlHelper;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * .
 * 
 * @author Danny(sunhao)(sunhao.java@gmail.com)
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
	        return new ClobStringSqlTypeValue(this.sqlHelper, (String)this.beanWrapper.getPropertyValue(paramType.getRealParamName()));

	      if (paramType.getValueType() == 2) {
	        return new LongStringSqlTypeValue(this.sqlHelper, (String)this.beanWrapper.getPropertyValue(paramType.getRealParamName()));
	      }

	      return this.beanWrapper.getPropertyValue(paramType.getRealParamName());
	    } catch (NotReadablePropertyException ex) {
	      throw new IllegalArgumentException(ex.getMessage());
	    }
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

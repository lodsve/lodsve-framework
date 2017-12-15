package lodsve.mybatis.datasource.builder;

import lodsve.mybatis.configs.Constant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 关系型数据库数据源.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/21 下午4:00
 */
public class RdbmsDataSourceFactory extends AbstractDataSource<DataSource> {
    public RdbmsDataSourceFactory(String dataSourceName) {
        super(dataSourceName);
    }

    @Override
    public DataSource build() {
        String dataSourceClassName = super.rdbmsProperties.getDataSourceClass();
        try {
            Object dataSource = BeanUtils.instantiateClass(ClassUtils.forName(dataSourceClassName, Thread.currentThread().getContextClassLoader()));

            BeanWrapper beanWrapper = new BeanWrapperImpl(dataSource);

            setDataSourceProperty(beanWrapper);

            DataSource ds = (DataSource) beanWrapper.getWrappedInstance();

            setCustomProperties(ds, dataSourceClassName);

            return (DataSource) beanWrapper.getWrappedInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void setDataSourceProperty(BeanWrapper beanWrapper) {
        setDataSourceProperty(beanWrapper, getProperties());
    }

    private void setDataSourceProperty(BeanWrapper beanWrapper, Map<String, String> properties) {
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            beanWrapper.setPropertyValue(entry.getKey(), entry.getValue());
        }
    }

    private void setCustomProperties(DataSource ds, String dataSourceClassName) {
        try {
            //1.druid
            if (Constant.DRUID_DATA_SOURCE_CLASS.equals(dataSourceClassName)) {
                // init method
                ds.getClass().getMethod("init").invoke(ds);
                // destroy method
                ds.getClass().getMethod("close").invoke(ds);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

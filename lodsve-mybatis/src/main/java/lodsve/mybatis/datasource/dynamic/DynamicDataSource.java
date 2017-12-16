package lodsve.mybatis.datasource.dynamic;

import lodsve.core.utils.StringUtils;
import lodsve.mybatis.exception.MyBatisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态数据源.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/12/14 下午6:23
 */
public class DynamicDataSource extends AbstractRoutingDataSource implements BeanFactoryAware {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);
    private List<String> dataSourceBeans;
    private String defaultDataSource;
    private BeanFactory beanFactory;

    public DynamicDataSource(List<String> dataSourceBeans, String defaultDataSource) {
        this.dataSourceBeans = dataSourceBeans;
        this.defaultDataSource = defaultDataSource;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceHolder.get();
    }

    @Override
    public void afterPropertiesSet() {
        if (this.dataSourceBeans == null) {
            throw new IllegalArgumentException("Property 'dataSourceBeans' is required");
        }

        Map<Object, Object> targetDataSources = new HashMap<>(dataSourceBeans.size());
        for (String dataSourceBean : dataSourceBeans) {
            Object object = getDataSourceByBeanName(dataSourceBean);

            targetDataSources.put(dataSourceBean, object);
        }

        super.setTargetDataSources(targetDataSources);

        if (StringUtils.isNotBlank(defaultDataSource)) {
            super.setDefaultTargetDataSource(getDataSourceByBeanName(defaultDataSource));
        }

        super.afterPropertiesSet();
    }

    private DataSource getDataSourceByBeanName(String beanName) {
        Object object = beanFactory.getBean(beanName);
        if (object == null || !(object instanceof DataSource)) {
            if (logger.isErrorEnabled()) {
                logger.error("The bean named '{}' is not a {}!", beanName, DataSource.class.getName());
            }
            throw new MyBatisException(102005, String.format("The bean named '%s' is not a '%s'!", beanName, DataSource.class.getName()));
        }

        return (DataSource) object;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}

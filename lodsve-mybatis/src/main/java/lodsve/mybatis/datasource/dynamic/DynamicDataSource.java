/*
 * Copyright (C) 2018  Sun.Hao
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
 * @author sunhao(sunhao.java @ gmail.com)
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
        return DataSourceHolder.getInstance().get();
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

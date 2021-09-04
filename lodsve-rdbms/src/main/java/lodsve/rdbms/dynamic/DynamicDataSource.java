/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lodsve.rdbms.dynamic;

import com.google.common.collect.Maps;
import lodsve.core.utils.StringUtils;
import lodsve.rdbms.exception.RdbmsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.NonNull;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * 动态数据源.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017/12/14 下午6:23
 */
public class DynamicDataSource extends AbstractRoutingDataSource implements BeanFactoryAware {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);
    private final List<String> dataSourceBeans;
    private final String defaultDataSource;
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

        Map<Object, Object> targetDataSources = Maps.newHashMap();
        dataSourceBeans.forEach(d -> targetDataSources.put(d, getDataSourceByBeanName(d)));

        super.setTargetDataSources(targetDataSources);

        if (StringUtils.isNotBlank(defaultDataSource)) {
            super.setDefaultTargetDataSource(getDataSourceByBeanName(defaultDataSource));
        }

        super.afterPropertiesSet();
    }

    private DataSource getDataSourceByBeanName(String beanName) {
        Object object = beanFactory.getBean(beanName);
        if (!(object instanceof DataSource)) {
            if (logger.isErrorEnabled()) {
                logger.error("The bean named '{}' is not a {}!", beanName, DataSource.class.getName());
            }
            throw new RdbmsException(108001, String.format("The bean named '%s' is not a '%s'!", beanName, DataSource.class.getName()));
        }

        return (DataSource) object;
    }

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}

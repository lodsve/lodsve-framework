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
package lodsve.rdbms.pool;

import com.google.common.collect.Maps;
import lodsve.core.autoproperties.relaxedbind.RelaxedBindFactory;
import lodsve.core.utils.BeanMapper;
import lodsve.rdbms.properties.PoolSetting;
import lodsve.rdbms.properties.RdbmsProperties;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

import java.util.Map;

/**
 * 数据源连接池.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
public abstract class BaseDataSourcePool {
    private static final String DATA_SOURCE_NAME_DBCP = "org.apache.commons.dbcp.BasicDataSource";
    private static final String DATA_SOURCE_NAME_DRUID = "com.alibaba.druid.pool.DruidDataSource";

    private final String dataSourceName;
    private final RdbmsProperties rdbmsProperties;

    BaseDataSourcePool(String dataSourceName, RdbmsProperties rdbmsProperties) {
        this.dataSourceName = dataSourceName;
        this.rdbmsProperties = rdbmsProperties;
    }

    /**
     * 创建数据源
     *
     * @return 数据源
     */
    public BeanDefinition build() {
        String dataSourceClassName = rdbmsProperties.getDataSourceClass();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(dataSourceClassName);

        setDataSourceProperty(beanDefinitionBuilder, getProperties());
        setCustomProperties(beanDefinitionBuilder, rdbmsProperties);

        return beanDefinitionBuilder.getBeanDefinition();
    }

    private Map<String, String> getProperties() {
        // 连接信息
        PoolSetting poolSetting = rdbmsProperties.getPool().get(dataSourceName);

        Map<String, String> properties = Maps.newHashMap();
        BeanMapper.map(poolSetting, properties);
        return properties;
    }

    private void setDataSourceProperty(BeanDefinitionBuilder dataSourceBuilder, Map<String, String> properties) {
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            dataSourceBuilder.addPropertyValue(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 设置不同连接池的私有属性
     *
     * @param beanDefinitionBuilder Bean Definition Builder
     * @param rdbmsProperties       rdbms Properties
     */
    public abstract void setCustomProperties(BeanDefinitionBuilder beanDefinitionBuilder, RdbmsProperties rdbmsProperties);

    /**
     * 根据数据源名称获取连接池生成器
     *
     * @param dataSourceName 数据源名称
     * @return 连接池生成器, 默认是dbcp
     */
    public static BaseDataSourcePool getDataSourcePool(String dataSourceName) {
        RdbmsProperties rdbmsProperties = new RelaxedBindFactory.Builder<>(RdbmsProperties.class).build();

        String dataSourceClassName = rdbmsProperties.getDataSourceClass();
        switch (dataSourceClassName) {
            case DATA_SOURCE_NAME_DBCP:
                return new DbcpDataSourcePool(dataSourceName, rdbmsProperties);
            case DATA_SOURCE_NAME_DRUID:
                return new DruidDataSourcePool(dataSourceName, rdbmsProperties);
            default:
                return new DbcpDataSourcePool(dataSourceName, rdbmsProperties);
        }
    }
}

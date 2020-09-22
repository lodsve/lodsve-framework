/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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

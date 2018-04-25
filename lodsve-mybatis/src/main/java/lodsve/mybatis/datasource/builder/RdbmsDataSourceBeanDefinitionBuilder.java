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

package lodsve.mybatis.datasource.builder;

import lodsve.core.properties.relaxedbind.RelaxedBindFactory;
import lodsve.mybatis.properties.DruidProperties;
import lodsve.mybatis.properties.PoolSetting;
import lodsve.mybatis.properties.RdbmsProperties;
import lodsve.mybatis.utils.Constants;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建数据源.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @date 2017/12/14 下午8:35
 */
public class RdbmsDataSourceBeanDefinitionBuilder {
    private String dataSourceName;
    private RdbmsProperties rdbmsProperties;

    public RdbmsDataSourceBeanDefinitionBuilder(String dataSourceName) {
        this.dataSourceName = dataSourceName;

        rdbmsProperties = new RelaxedBindFactory.Builder<>(RdbmsProperties.class).build();
    }

    /**
     * 创建数据源
     *
     * @return 数据源
     */
    public BeanDefinition build() {
        String dataSourceClassName = rdbmsProperties.getDataSourceClass();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(dataSourceClassName);

        setDataSourceProperty(beanDefinitionBuilder);
        setCustomProperties(beanDefinitionBuilder, dataSourceClassName);

        return beanDefinitionBuilder.getBeanDefinition();
    }

    private Map<String, String> getProperties() {
        // 连接信息
        PoolSetting poolSetting = rdbmsProperties.getPool().get(dataSourceName);

        Map<String, String> properties = new HashMap<>(16);
        properties.putAll(toMap(poolSetting));

        return properties;
    }

    private Map<String, String> toMap(Object object) {
        BeanWrapper wrapper = new BeanWrapperImpl(object);
        PropertyDescriptor[] descriptors = wrapper.getPropertyDescriptors();
        Map<String, String> properties = new HashMap<>(descriptors.length);
        for (PropertyDescriptor d : descriptors) {
            if (d.getWriteMethod() == null) {
                continue;
            }

            String name = d.getName();
            Object value = wrapper.getPropertyValue(name);

            properties.put(name, value.toString());
        }

        return properties;
    }

    private void setDataSourceProperty(BeanDefinitionBuilder dataSourceBuilder) {
        setDataSourceProperty(dataSourceBuilder, getProperties());
    }

    private void setDataSourceProperty(BeanDefinitionBuilder dataSourceBuilder, Map<String, String> properties) {
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            dataSourceBuilder.addPropertyValue(entry.getKey(), entry.getValue());
        }
    }

    private void setCustomProperties(BeanDefinitionBuilder beanDefinitionBuilder, String dataSourceClassName) {
        //1.druid
        if (Constants.DRUID_DATA_SOURCE_CLASS.equals(dataSourceClassName)) {
            // init method
            beanDefinitionBuilder.setInitMethodName("init");
            // destroy method
            beanDefinitionBuilder.setDestroyMethodName("close");

            DruidProperties druidProperties = new RelaxedBindFactory.Builder<>(DruidProperties.class).build();
            String filters = druidProperties.getFilters();
            beanDefinitionBuilder.addPropertyValue("filters", filters);
        }
    }
}

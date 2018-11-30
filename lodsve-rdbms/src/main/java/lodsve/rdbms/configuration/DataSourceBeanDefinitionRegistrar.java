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

package lodsve.rdbms.configuration;

import com.google.common.collect.Maps;
import com.p6spy.engine.spy.P6DataSource;
import lodsve.core.bean.BeanRegisterUtils;
import lodsve.core.properties.Profiles;
import lodsve.rdbms.Constants;
import lodsve.rdbms.annotations.DataSourceProvider;
import lodsve.rdbms.dynamic.DynamicDataSource;
import lodsve.rdbms.exception.RdbmsException;
import lodsve.rdbms.p6spy.LodsveP6OptionsSource;
import lodsve.rdbms.pool.BaseDataSourcePool;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态创建数据源的配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/1/19 下午8:01
 */
public class DataSourceBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata metadata, @NonNull BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(DataSourceProvider.class.getName(), false));
        Assert.notNull(attributes, "No @DataSourceProvider provide!");

        Map<String, BeanDefinition> beanDefinitions = Maps.newHashMap();
        String[] dataSources = attributes.getStringArray(Constants.DATA_SOURCE_ATTRIBUTE_NAME);

        if (dataSources.length == 0) {
            throw new RdbmsException(102005, "can't find any datasource!");
        }

        // 组装一些信息
        DataSourceBean dataSourceBean = new DataSourceBean(dataSources);
        String defaultDataSourceKey = dataSourceBean.getDefaultDataSourceKey();
        beanDefinitions.putAll(dataSourceBean.getBeanDefinitions());

        // 动态数据源
        BeanDefinitionBuilder dynamicDataSource = BeanDefinitionBuilder.genericBeanDefinition(DynamicDataSource.class);
        dynamicDataSource.addConstructorArgValue(dataSourceBean.getDataSourceNames());
        dynamicDataSource.addConstructorArgValue(defaultDataSourceKey);

        boolean p6spy = Profiles.getProfile("p6spy");

        String dataSourceBeanName = p6spy ? Constants.REAL_DATA_SOURCE_BEAN_NAME : Constants.DATA_SOURCE_BEAN_NAME;
        beanDefinitions.put(dataSourceBeanName, dynamicDataSource.getBeanDefinition());
        if (p6spy) {
            // 使用p6spy
            BeanDefinitionBuilder p6spyDataSource = BeanDefinitionBuilder.genericBeanDefinition(P6DataSource.class);
            p6spyDataSource.addConstructorArgReference(Constants.REAL_DATA_SOURCE_BEAN_NAME);

            beanDefinitions.put(Constants.DATA_SOURCE_BEAN_NAME, p6spyDataSource.getBeanDefinition());

            // 初始化配置
            LodsveP6OptionsSource.init();
        }

        BeanRegisterUtils.registerBeans(beanDefinitions, registry);
    }

    private static class DataSourceBean {
        private String defaultDataSourceKey;
        private Map<String, BeanDefinition> beanDefinitions;
        private List<String> dataSourceNames;

        DataSourceBean(String[] dataSources) {
            beanDefinitions = new HashMap<>(dataSources.length);
            dataSourceNames = new ArrayList<>(dataSources.length);

            for (int i = 0; i < dataSources.length; i++) {
                String name = dataSources[i];
                BeanDefinition dsBeanDefinition = BaseDataSourcePool.getDataSourcePool(name).build();

                if (i == 0) {
                    defaultDataSourceKey = name;
                }

                beanDefinitions.put(name, dsBeanDefinition);
                dataSourceNames.add(name);
            }
        }

        String getDefaultDataSourceKey() {
            return defaultDataSourceKey;
        }

        Map<String, BeanDefinition> getBeanDefinitions() {
            return beanDefinitions;
        }

        List<String> getDataSourceNames() {
            return dataSourceNames;
        }
    }
}

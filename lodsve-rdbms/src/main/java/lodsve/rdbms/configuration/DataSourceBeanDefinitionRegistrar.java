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
package lodsve.rdbms.configuration;

import com.google.common.collect.Maps;
import com.p6spy.engine.spy.P6DataSource;
import lodsve.core.autoproperties.Profiles;
import lodsve.core.bean.BeanRegisterUtils;
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
            throw new RdbmsException(108001, "can't find any datasource!");
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
        private final Map<String, BeanDefinition> beanDefinitions;
        private final List<String> dataSourceNames;

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

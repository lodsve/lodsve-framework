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

import lodsve.core.condition.ConditionalOnProperty;
import lodsve.rdbms.Constants;
import lodsve.rdbms.properties.RdbmsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

/**
 * 配置事务.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016/1/20 11:54
 */
@Configuration
@EnableTransactionManagement
@ConditionalOnProperty(clazz = RdbmsProperties.class, key = "supportTransaction", value = "true")
public class DataSourceTransactionManagementConfiguration implements TransactionManagementConfigurer {
    private final DataSource dataSource;

    @Autowired
    public DataSourceTransactionManagementConfiguration(@Qualifier(Constants.DATA_SOURCE_BEAN_NAME) DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    @Override
    @NonNull
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return txManager();
    }
}

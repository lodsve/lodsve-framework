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
package lodsve.mybatis.configuration;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.List;

/**
 * message-mybatis配置包扫描路径.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/1/19 下午10:21
 */
@org.springframework.context.annotation.Configuration
public class MyBatisConfiguration {
    private static final String DATA_SOURCE_BEAN_NAME = "lodsveDataSource";
    private final List<ConfigurationCustomizer> customizers;
    private final Interceptor[] interceptors;

    public MyBatisConfiguration(ObjectProvider<List<ConfigurationCustomizer>> customizers, ObjectProvider<Interceptor[]> interceptors) {
        this.customizers = customizers.getIfAvailable();
        this.interceptors = interceptors.getIfAvailable();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier(DATA_SOURCE_BEAN_NAME) DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        if (ArrayUtils.isNotEmpty(interceptors)) {
            factory.setPlugins(interceptors);
        }

        Configuration configuration = new Configuration();
        customizers.forEach(c -> c.customize(configuration));
        factory.setConfiguration(configuration);

        return factory.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}

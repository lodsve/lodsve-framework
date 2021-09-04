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
package lodsve.mongodb.config;

import lodsve.core.autoproperties.relaxedbind.annotations.EnableConfigurationProperties;
import lodsve.mongodb.Constants;
import lodsve.mongodb.core.MongoRepositoryBeanPostProcessor;
import lodsve.mongodb.properties.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;

/**
 * 基本配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/1/21 下午10:15
 */
@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties(MongoProperties.class)
@ComponentScan("lodsve.mongodb.connection")
public class MongoConfiguration {
    @Bean(name = Constants.DEFAULT_MONGO_TYPE_MAPPER_BEAN_NAME)
    public DefaultMongoTypeMapper defaultMongoTypeMapper() {
        return new DefaultMongoTypeMapper();
    }

    @Bean
    public MongoRepositoryBeanPostProcessor beanPostProcessor() {
        return new MongoRepositoryBeanPostProcessor();
    }
}

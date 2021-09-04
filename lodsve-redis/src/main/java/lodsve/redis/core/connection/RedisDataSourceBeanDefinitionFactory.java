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
package lodsve.redis.core.connection;

import lodsve.core.autoproperties.relaxedbind.RelaxedBindFactory;
import lodsve.redis.core.properties.RedisProperties;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

/**
 * redis数据源.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/1/21 下午4:10
 */
public class RedisDataSourceBeanDefinitionFactory {
    private final String dataSourceName;
    private final RedisProperties redisProperties;

    public RedisDataSourceBeanDefinitionFactory(String dataSourceName) {
        this.dataSourceName = dataSourceName;

        this.redisProperties = new RelaxedBindFactory.Builder<>(RedisProperties.class).build();
    }

    public BeanDefinition build() {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(LodsveRedisConnectionFactory.class);

        beanDefinitionBuilder.addConstructorArgValue(dataSourceName);
        beanDefinitionBuilder.addConstructorArgValue(redisProperties);

        return beanDefinitionBuilder.getBeanDefinition();
    }
}

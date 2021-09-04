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
package lodsve.redis.core.annotations;

import lodsve.redis.core.connection.RedisDataSourceBeanDefinitionFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * redis数据源注册.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/1/23 下午11:20
 */
public class RedisBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    private static final String DATASOURCE_NAME_ATTRIBUTE_NAME = "name";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableRedis.class.getName(), false));
        String[] names = attributes.getStringArray(DATASOURCE_NAME_ATTRIBUTE_NAME);

        for (String name : names) {
            if (beanDefinitionRegistry.containsBeanDefinition(name)) {
                continue;
            }

            beanDefinitionRegistry.registerBeanDefinition(name, new RedisDataSourceBeanDefinitionFactory(name).build());
        }
    }
}

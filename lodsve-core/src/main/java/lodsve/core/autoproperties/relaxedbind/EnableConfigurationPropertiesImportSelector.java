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
package lodsve.core.autoproperties.relaxedbind;

import lodsve.core.autoproperties.relaxedbind.annotations.ConfigurationProperties;
import lodsve.core.autoproperties.relaxedbind.annotations.EnableConfigurationProperties;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 启用ConfigurationProperties.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016/12/27 上午11:37
 */
public class EnableConfigurationPropertiesImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{ConfigurationPropertiesBeanRegistrar.class.getName(), ConfigurationPropertiesBindingPostProcessorRegistrar.class.getName()};
    }

    /**
     * 注册properties类到spring上下文
     */
    public static class ConfigurationPropertiesBeanRegistrar implements ImportBeanDefinitionRegistrar {
        @Override
        public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
            MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(EnableConfigurationProperties.class.getName(), false);
            List<Class<?>> types = collectClasses(attributes.get("value"));
            for (Class<?> type : types) {
                String prefix = extractPrefix(type);
                String name = (StringUtils.hasText(prefix) ? prefix + "-" + type.getName() : type.getName());
                if (!registry.containsBeanDefinition(name)) {
                    registerBeanDefinition(registry, type, name);
                }
            }
        }

        private String extractPrefix(Class<?> type) {
            ConfigurationProperties annotation = AnnotationUtils.findAnnotation(type, ConfigurationProperties.class);
            if (annotation != null) {
                return annotation.prefix();
            }
            return "";
        }

        private List<Class<?>> collectClasses(List<Object> list) {
            ArrayList<Class<?>> result = new ArrayList<>();
            for (Object object : list) {
                for (Object value : (Object[]) object) {
                    if (value instanceof Class && value != void.class) {
                        result.add((Class<?>) value);
                    }
                }
            }

            List<String> names = SpringFactoriesLoader.loadFactoryNames(EnableConfigurationProperties.class, Thread.currentThread().getContextClassLoader());
            for (String name : names) {
                Class<?> clazz;
                try {
                    clazz = ClassUtils.forName(name, Thread.currentThread().getContextClassLoader());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    continue;
                }

                result.add(clazz);
            }
            return result;
        }

        private void registerBeanDefinition(BeanDefinitionRegistry registry, Class<?> type, String name) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(type);
            BeanDefinition beanDefinition = builder.getBeanDefinition();

            registry.registerBeanDefinition(name, beanDefinition);

            ConfigurationProperties properties = AnnotationUtils.findAnnotation(type, ConfigurationProperties.class);
            Assert.notNull(properties, "No " + ConfigurationProperties.class.getSimpleName() + " annotation found on  '" + type.getName() + "'.");
        }

    }
}

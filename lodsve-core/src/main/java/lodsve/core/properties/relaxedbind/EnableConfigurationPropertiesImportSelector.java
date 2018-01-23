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

package lodsve.core.properties.relaxedbind;

import lodsve.core.properties.relaxedbind.annotations.ConfigurationProperties;
import lodsve.core.properties.relaxedbind.annotations.EnableConfigurationProperties;
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
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2016/12/27 上午11:37
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

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
package lodsve.mybatis.annotations;

import com.google.common.collect.Lists;
import lodsve.mybatis.configuration.LodsveConfigurationCustomizer;
import lodsve.mybatis.utils.DbType;
import lodsve.mybatis.utils.MyBatisUtils;
import org.mybatis.spring.annotation.MapperScannerRegistrar;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * 扩展{@link MapperScannerRegistrar}.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-11-27 22:56
 */
public class MybatisConfigsRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private static final String KEY_ANNOTATION_CLASS = "annotationClass";
    private static final String KEY_BASE_PACKAGES = "basePackages";
    private static final String KEY_MAP_UNDERSCORE_TO_CAMEL_CASE = "mapUnderscoreToCamelCase";
    private static final String KEY_ENUMS_LOCATIONS = "enumsLocations";
    private static final String BEAN_NAME_CONFIGURATION_CUSTOMIZER = "configurationCustomizerBean";
    private static final String KEY_DB_TYPE = "type";

    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableMyBatis.class.getName()));
        if (null == attributes) {
            return;
        }
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);

        // this check is needed in Spring 3.1
        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }

        Class<? extends Annotation> annotationClass = attributes.getClass(KEY_ANNOTATION_CLASS);
        if (!Annotation.class.equals(annotationClass)) {
            scanner.setAnnotationClass(annotationClass);
        }

        List<String> basePackages = Lists.newArrayList();
        Arrays.stream(attributes.getStringArray(KEY_BASE_PACKAGES)).filter(StringUtils::hasText).forEach(basePackages::add);

        scanner.registerFilters();
        scanner.doScan(StringUtils.toStringArray(basePackages));

        // 处理自定义的
        boolean mapUnderscoreToCamelCase = attributes.getBoolean(KEY_MAP_UNDERSCORE_TO_CAMEL_CASE);
        String[] enumsLocations = attributes.getStringArray(KEY_ENUMS_LOCATIONS);

        BeanDefinitionBuilder xnyConfigurationCustomizerBean = BeanDefinitionBuilder.genericBeanDefinition(LodsveConfigurationCustomizer.class);
        xnyConfigurationCustomizerBean.addConstructorArgValue(mapUnderscoreToCamelCase);
        xnyConfigurationCustomizerBean.addConstructorArgValue(enumsLocations);

        registry.registerBeanDefinition(BEAN_NAME_CONFIGURATION_CUSTOMIZER, xnyConfigurationCustomizerBean.getBeanDefinition());

        // 处理数据库方言
        DbType dbType = attributes.getEnum(KEY_DB_TYPE);
        MyBatisUtils.setDbType(dbType);
    }
}

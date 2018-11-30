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

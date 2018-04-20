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

package lodsve.web.springfox.annotations;

import lodsve.core.properties.Profiles;
import lodsve.web.springfox.config.SpringFoxDocket;
import lodsve.web.springfox.paths.SpringFoxPathProvider;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * 用来注册spring fox路径处理器.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @date 16/3/24 上午9:52
 */
public class SpringFoxRegistrar implements ImportBeanDefinitionRegistrar {
    private static final String PREFIX_ATTRIBUTE_NAME = "prefix";
    private static final String GROUPS_ATTRIBUTE_NAME = "groups";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Boolean enableSpringfox = Profiles.getProfile("springfox");

        if (!enableSpringfox) {
            return;
        }

        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableSpringFox.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", EnableSpringFox.class.getName(), importingClassMetadata.getClassName()));

        String prefix = attributes.getString(PREFIX_ATTRIBUTE_NAME);

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(SpringFoxPathProvider.class);
        builder.addPropertyValue("prefix", prefix);

        registry.registerBeanDefinition(SpringFoxPathProvider.class.getName(), builder.getBeanDefinition());

        String[] groups = attributes.getStringArray(GROUPS_ATTRIBUTE_NAME);
        for (String group : groups) {
            BeanDefinitionBuilder docket = BeanDefinitionBuilder.genericBeanDefinition(SpringFoxDocket.class);
            docket.addConstructorArgValue(group);

            registry.registerBeanDefinition(group + "Docket", docket.getBeanDefinition());
        }
    }
}

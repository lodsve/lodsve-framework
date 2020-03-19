/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
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

package lodsve.dfs.configuration;

import lodsve.dfs.annotations.EnableDfs;
import lodsve.dfs.enums.DfsType;
import lodsve.dfs.service.DfsService;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * 动态注册bean.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017-12-4-0004 11:20
 */
public class DfsBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    private static final String VALUE_ATTRIBUTE_NAME = "value";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableDfs.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", EnableDfs.class.getName(), annotationMetadata.getClassName()));

        DfsType type = attributes.getEnum(VALUE_ATTRIBUTE_NAME);
        Class<? extends DfsService> clazz = type != null ? type.getImplClazz() : null;

        if (clazz == null) {
            return;
        }

        BeanDefinitionBuilder fsServiceBean = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        beanDefinitionRegistry.registerBeanDefinition("fsService", fsServiceBean.getBeanDefinition());
    }
}

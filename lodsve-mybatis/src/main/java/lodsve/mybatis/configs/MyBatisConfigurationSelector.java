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

package lodsve.mybatis.configs;

import lodsve.mybatis.configs.annotations.EnableMyBatis;
import lodsve.mybatis.datasource.DataSourceTransactionManagementConfiguration;
import lodsve.mybatis.utils.Constants;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * 判断是否启用事务，并加载.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016/1/20 11:31
 */
public class MyBatisConfigurationSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        List<String> imports = new ArrayList<>();
        imports.add(MyBatisBeanDefinitionRegistrar.class.getName());
        imports.add(MyBatisConfiguration.class.getName());

        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableMyBatis.class.getName(), false));
        boolean supportTransaction = attributes.getBoolean(Constants.SUPPORT_TRANSACTION_ATTRIBUTE_NAME);
        if (supportTransaction) {
            imports.add(DataSourceTransactionManagementConfiguration.class.getName());
        }

        return imports.toArray(new String[imports.size()]);
    }
}

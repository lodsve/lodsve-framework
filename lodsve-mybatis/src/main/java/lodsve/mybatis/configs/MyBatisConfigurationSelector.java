package lodsve.mybatis.configs;

import lodsve.mybatis.configs.annotations.EnableMyBatis;
import lodsve.mybatis.datasource.DataSourceTransactionManagementConfiguration;
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
    private static final String SUPPORT_TRANSACTION_ATTRIBUTE_NAME = "supportTransaction";

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        List<String> imports = new ArrayList<>();
        imports.add(MyBatisBeanDefinitionRegistrar.class.getName());
        imports.add(MyBatisConfiguration.class.getName());

        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableMyBatis.class.getName(), false));
        boolean supportTransaction = attributes.getBoolean(SUPPORT_TRANSACTION_ATTRIBUTE_NAME);
        if (supportTransaction) {
            imports.add(DataSourceTransactionManagementConfiguration.class.getName());
        }

        return imports.toArray(new String[imports.size()]);
    }
}

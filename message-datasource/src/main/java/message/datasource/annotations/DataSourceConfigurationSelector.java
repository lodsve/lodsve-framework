package message.datasource.annotations;

import message.datasource.core.DataSourceTransactionManagementConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016/1/20 11:31
 */
public class DataSourceConfigurationSelector implements ImportSelector {
    private static final String SUPPORT_TRANSACTION_ATTRIBUTE_NAME = "supportTransaction";

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(DataSource.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", DataSource.class.getName(), importingClassMetadata.getClassName()));

        List<String> imports = new ArrayList<>();
        imports.add(DataSourceImportBeanDefinitionRegistrar.class.getName());
        boolean supportTransaction = attributes.getBoolean(SUPPORT_TRANSACTION_ATTRIBUTE_NAME);
        if (supportTransaction) {
            imports.add(DataSourceTransactionManagementConfiguration.class.getName());
        }

        return imports.toArray(new String[imports.size()]);
    }
}

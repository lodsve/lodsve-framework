package message.datasource.annotations;

import message.datasource.config.DataSourceTransactionManagementConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * 判断是否启用事务，并加载.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016/1/20 11:31
 */
public class DataSourceConfigurationSelector implements ImportSelector {
    private static final String SUPPORT_TRANSACTION_ATTRIBUTE_NAME = "supportTransaction";
    public static final String DATASOURCE_TYPE_ATTRIBUTE_NAME = "type";

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(DataSource.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", DataSource.class.getName(), importingClassMetadata.getClassName()));

        DataSourceType type = attributes.getEnum(DATASOURCE_TYPE_ATTRIBUTE_NAME);

        List<String> imports = new ArrayList<>();
        imports.add(DataSourceImportBeanDefinitionRegistrar.class.getName());
        boolean supportTransaction = attributes.getBoolean(SUPPORT_TRANSACTION_ATTRIBUTE_NAME);
        if (DataSourceType.RDBMS.equals(type) && supportTransaction) {
            imports.add(DataSourceTransactionManagementConfiguration.class.getName());
        }

        return imports.toArray(new String[imports.size()]);
    }
}

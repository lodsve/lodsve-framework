package message.datasource.annotations;

import message.datasource.core.DataSourceBeanDefinitionFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * 通过注解的方式加载数据源.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/19 下午7:20
 */
public class DataSourceImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    public static final String DATASOURCE_NAME_ATTRIBUTE_NAME = "name";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(DataSource.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", DataSource.class.getName(), importingClassMetadata.getClassName()));

        String name = attributes.getString(DATASOURCE_NAME_ATTRIBUTE_NAME);
        Assert.hasText(name, String.format("@%s don't provide any datasource name!", DataSource.class.getName()));

        if (registry.containsBeanDefinition(name)) {
            return;
        }

        BeanDefinition beanDefinition = new DataSourceBeanDefinitionFactory(name).build();
        registry.registerBeanDefinition(name, beanDefinition);
    }
}

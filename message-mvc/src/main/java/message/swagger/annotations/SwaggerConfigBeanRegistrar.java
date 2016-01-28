package message.swagger.annotations;

import message.swagger.config.CosmosPathProvider;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/28 下午5:15
 */
public class SwaggerConfigBeanRegistrar implements ImportBeanDefinitionRegistrar {
    private static final String BASE_PATH_ATTRIBUTE_NAME = "basePath";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableCosmosSwagger.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", EnableCosmosSwagger.class.getName(), importingClassMetadata.getClassName()));

        String basePath = attributes.getString(BASE_PATH_ATTRIBUTE_NAME);
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(CosmosPathProvider.class);
        builder.addConstructorArgValue(basePath);

        registry.registerBeanDefinition(CosmosPathProvider.class.getName(), builder.getBeanDefinition());
    }
}

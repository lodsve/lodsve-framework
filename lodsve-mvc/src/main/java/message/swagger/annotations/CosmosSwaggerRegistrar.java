package message.swagger.annotations;

import message.swagger.paths.CosmosSwaggerPathProvider;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * 用来注册swagger路径处理器.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/3 下午12:54
 */
public class CosmosSwaggerRegistrar implements ImportBeanDefinitionRegistrar {
    private static final String PREFIX_ATTRIBUTE_NAME = "prefix";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableCosmosSwagger.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", EnableCosmosSwagger.class.getName(), importingClassMetadata.getClassName()));

        String prefix = attributes.getString(PREFIX_ATTRIBUTE_NAME);

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(CosmosSwaggerPathProvider.class);
        builder.addConstructorArgValue(prefix);

        registry.registerBeanDefinition(CosmosSwaggerPathProvider.class.getName(), builder.getBeanDefinition());
    }
}

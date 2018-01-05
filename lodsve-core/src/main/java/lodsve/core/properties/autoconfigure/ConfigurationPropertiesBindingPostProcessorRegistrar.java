package lodsve.core.properties.autoconfigure;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 注册ConfigurationPropertiesBindingPostProcessor的bean到spring上下文.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016/12/27 上午11:50
 */
public class ConfigurationPropertiesBindingPostProcessorRegistrar implements ImportBeanDefinitionRegistrar {
    /**
     * The bean name of the {@link ConfigurationPropertiesBindingPostProcessor}.
     */
    private static final String BINDER_BEAN_NAME = ConfigurationPropertiesBindingPostProcessor.class.getName();

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        if (!registry.containsBeanDefinition(BINDER_BEAN_NAME)) {
            BeanDefinitionBuilder bean = BeanDefinitionBuilder.genericBeanDefinition(ConfigurationPropertiesBindingPostProcessor.class);
            registry.registerBeanDefinition(BINDER_BEAN_NAME, bean.getBeanDefinition());
        }
    }
}

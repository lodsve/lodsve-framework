package lodsve.dubbo.annotations;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import lodsve.core.autoconfigure.AutoConfigurationCreator;
import lodsve.core.autoconfigure.annotations.ConfigurationProperties;
import lodsve.dubbo.config.DubboProperties;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 加载dubbo的配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017-11-28-0028 14:17
 */
public class DubboBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    private static final String PRODUCERS_ATTRIBUTE_NAME = "producers";

    private Map<String, BeanDefinition> beanDefinitions = new HashMap<>(16);
    private String application;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableDubbo.class.getName(), false));
        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", EnableDubbo.class.getName(), annotationMetadata.getClassName()));

        String[] producers = attributes.getStringArray(PRODUCERS_ATTRIBUTE_NAME);

        DubboProperties properties = getDubboProperties();

        if (properties == null) {
            return;
        }

        application = properties.getApplication();
        // 1. 生成ApplicationConfig
        generateApplication();
        // 2. 生成ProtocolConfig
        generateProtocolConfig(properties.getProtocol(), properties.getPort(), properties.getThreads());
        // 3. 生成RegistryConfig
        generateRegistryConfig(properties.getRegistry());

        registerBeanDefinitions(beanDefinitionRegistry);
    }

    private void generateRegistryConfig(String registry) {
        BeanDefinitionBuilder registryConfig = BeanDefinitionBuilder.genericBeanDefinition(RegistryConfig.class);
        registryConfig.addPropertyValue("address", registry);

        beanDefinitions.put(application + "RegistryConfig", registryConfig.getBeanDefinition());
    }

    private void generateProtocolConfig(String protocol, Integer port, Integer threads) {
        BeanDefinitionBuilder protocolConfig = BeanDefinitionBuilder.genericBeanDefinition(ProtocolConfig.class);
        protocolConfig.addPropertyValue("name", protocol);
        protocolConfig.addPropertyValue("port", port);
        protocolConfig.addPropertyValue("threads", threads);

        beanDefinitions.put(application + "ProtocolConfig", protocolConfig.getBeanDefinition());
    }

    private void generateApplication() {
        BeanDefinitionBuilder applicationConfig = BeanDefinitionBuilder.genericBeanDefinition(ApplicationConfig.class);
        applicationConfig.addPropertyValue("name", application);

        beanDefinitions.put(application + "Dubbo", applicationConfig.getBeanDefinition());
    }

    private void registerBeanDefinitions(BeanDefinitionRegistry registry) {
        Set<String> beanNames = beanDefinitions.keySet();
        for (String beanName : beanNames) {
            BeanDefinition bean = beanDefinitions.get(beanName);

            if (registry.containsBeanDefinition(beanName)) {
                continue;
            }

            registry.registerBeanDefinition(beanName, bean);
        }
    }

    private DubboProperties getDubboProperties() {
        AutoConfigurationCreator.Builder<DubboProperties> builder = new AutoConfigurationCreator.Builder<>();
        builder.setAnnotation(DubboProperties.class.getAnnotation(ConfigurationProperties.class));
        builder.setClazz(DubboProperties.class);

        try {
            return builder.build();
        } catch (Exception e) {
            return null;
        }
    }
}

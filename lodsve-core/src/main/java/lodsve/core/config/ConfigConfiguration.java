package lodsve.core.config;

import lodsve.core.config.i18n.DefaultResourceBundleMessageSource;
import lodsve.core.config.i18n.ResourceBundleHolder;
import lodsve.core.config.properties.ConfigurationLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/14 下午8:49
 */
@Configuration
public class ConfigConfiguration {

    @Bean
    public ResourceBundleHolder resourceBundleHolder() {
        return new ResourceBundleHolder();
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() throws Exception {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setProperties(ConfigurationLoader.getConfigProperties());

        return configurer;
    }

    @Bean
    public DefaultResourceBundleMessageSource messageSource() {
        DefaultResourceBundleMessageSource messageSource = new DefaultResourceBundleMessageSource();
        messageSource.setAlwaysUseMessageFormat(true);
        messageSource.setResourceBundleHolder(resourceBundleHolder());

        return messageSource;
    }
}

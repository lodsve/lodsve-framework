package message.config;

import message.config.loader.i18n.DefaultResourceBundleMessageSource;
import message.config.loader.i18n.ResourceBundleHolder;
import message.config.loader.properties.ConfigurationLoader;
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
    public ConfigurationLoader configurationLoader() throws Exception {
        ConfigurationLoader configurationLoader = new ConfigurationLoader();
        configurationLoader.afterPropertiesSet();

        return configurationLoader;
    }

    @Bean
    public ResourceBundleHolder resourceBundleHolder(){
        return new ResourceBundleHolder();
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() throws Exception {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setProperties(configurationLoader().getObject());

        return configurer;
    }

    @Bean
    public DefaultResourceBundleMessageSource messageSource(){
        DefaultResourceBundleMessageSource messageSource = new DefaultResourceBundleMessageSource();
        messageSource.setAlwaysUseMessageFormat(true);
        messageSource.setResourceBundleHolder(resourceBundleHolder());

        return messageSource;
    }
}

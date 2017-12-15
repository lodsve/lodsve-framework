package lodsve.core.config.core;

import lodsve.core.config.i18n.DefaultResourceBundleMessageSource;
import lodsve.core.config.i18n.ResourceBundleHolder;
import lodsve.core.config.ini.IniLoader;
import lodsve.core.config.properties.ConfigurationLoader;
import lodsve.core.utils.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.Properties;

/**
 * .
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/1/14 下午8:49
 */
@Configuration
public class SpringContextConfigLoader {

    @Bean
    public ResourceBundleHolder resourceBundleHolder() {
        return new ResourceBundleHolder();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer placeholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        placeholderConfigurer.setFileEncoding("UTF-8");

        Properties properties = ConfigurationLoader.getConfigProperties();
        if (properties.isEmpty()) {
            ParamsHome.getInstance().init(StringUtils.EMPTY);
            ConfigurationLoader.init();
            IniLoader.init();

            properties = ConfigurationLoader.getConfigProperties();
        }

        placeholderConfigurer.setProperties(properties);

        return placeholderConfigurer;
    }

    @Bean
    public DefaultResourceBundleMessageSource messageSource() {
        DefaultResourceBundleMessageSource messageSource = new DefaultResourceBundleMessageSource();
        messageSource.setAlwaysUseMessageFormat(true);
        messageSource.setResourceBundleHolder(resourceBundleHolder());

        return messageSource;
    }
}

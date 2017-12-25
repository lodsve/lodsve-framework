package lodsve.core.configuration;

import lodsve.core.properties.init.ParamsHome;
import lodsve.core.properties.env.EnvLoader;
import lodsve.core.properties.i18n.DefaultResourceBundleMessageSource;
import lodsve.core.properties.i18n.ResourceBundleHolder;
import lodsve.core.properties.ini.IniLoader;
import lodsve.core.utils.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.Properties;

/**
 * spring context.
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

        Properties properties = EnvLoader.getEnvs();
        if (properties.isEmpty()) {
            ParamsHome.getInstance().init(StringUtils.EMPTY);
            EnvLoader.init();
            IniLoader.init();

            properties = EnvLoader.getEnvs();
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

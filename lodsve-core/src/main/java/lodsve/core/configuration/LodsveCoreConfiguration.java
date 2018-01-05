package lodsve.core.configuration;

import lodsve.core.application.ApplicationProperties;
import lodsve.core.condition.ConditionalOnClass;
import lodsve.core.context.ApplicationContextListener;
import lodsve.core.email.EmailProperties;
import lodsve.core.event.EventExecutor;
import lodsve.core.event.EventPublisher;
import lodsve.core.properties.autoconfigure.annotations.EnableConfigurationProperties;
import lodsve.core.properties.env.EnvLoader;
import lodsve.core.properties.ini.IniLoader;
import lodsve.core.properties.init.ParamsHome;
import lodsve.core.properties.message.DefaultResourceBundleMessageSource;
import lodsve.core.properties.message.ResourceBundleHolder;
import lodsve.core.utils.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import javax.mail.MessagingException;
import java.util.Properties;

/**
 * 配置core模块的properties.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2016/12/27 下午3:07
 */
@Configuration
@EnableConfigurationProperties({ApplicationProperties.class})
@ComponentScan({
        "lodsve.core.exception",
        "lodsve.core.properties"
})
public class LodsveCoreConfiguration {
    @Bean
    public ApplicationContextListener applicationContextListener() {
        return new ApplicationContextListener();
    }

    @Bean
    public EventExecutor eventExecutor() throws Exception {
        EventExecutor eventExecutor = new EventExecutor();
        eventExecutor.afterPropertiesSet();
        return eventExecutor;
    }

    @Bean
    public EventPublisher eventPublisher() {
        return new EventPublisher();
    }

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
    public DefaultResourceBundleMessageSource messageSource(ResourceBundleHolder resourceBundleHolder) {
        DefaultResourceBundleMessageSource messageSource = new DefaultResourceBundleMessageSource();
        messageSource.setAlwaysUseMessageFormat(true);
        messageSource.setResourceBundleHolder(resourceBundleHolder);

        return messageSource;
    }

    @Configuration
    @EnableConfigurationProperties(EmailProperties.class)
    @ConditionalOnClass(MessagingException.class)
    @ComponentScan("lodsve.core.email")
    public static class LodsveMail {
    }
}

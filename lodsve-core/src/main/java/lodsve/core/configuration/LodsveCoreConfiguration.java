/*
 * Copyright (C) 2018  Sun.Hao
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.core.configuration;

import lodsve.core.condition.ConditionalOnClass;
import lodsve.core.context.ApplicationContextListener;
import lodsve.core.event.EventExecutor;
import lodsve.core.event.EventPublisher;
import lodsve.core.properties.Env;
import lodsve.core.properties.Ini;
import lodsve.core.properties.message.DefaultResourceBundleMessageSource;
import lodsve.core.properties.message.ResourceBundleHolder;
import lodsve.core.properties.profile.ProfileInitializer;
import lodsve.core.properties.relaxedbind.annotations.EnableConfigurationProperties;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

import javax.mail.MessagingException;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 配置core模块的properties.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016/12/27 下午3:07
 */
@Configuration
@EnableConfigurationProperties({ApplicationProperties.class, MailProperties.class})
@ComponentScan({
        "lodsve.core.exception",
        "lodsve.core.properties"
})
public class LodsveCoreConfiguration {
    private final ApplicationProperties applicationProperties;

    public LodsveCoreConfiguration(ObjectProvider<ApplicationProperties> applicationProperties) {
        this.applicationProperties = applicationProperties.getIfAvailable();
    }

    @Bean
    public ThreadPoolExecutorFactoryBean threadPoolExecutorFactoryBean() {
        ThreadConfig config = applicationProperties.getThread();

        ThreadPoolExecutorFactoryBean threadPoolExecutorFactoryBean = new ThreadPoolExecutorFactoryBean();
        threadPoolExecutorFactoryBean.setCorePoolSize(config.getCorePoolSize());
        threadPoolExecutorFactoryBean.setMaxPoolSize(config.getMaxPoolSize());
        threadPoolExecutorFactoryBean.setAllowCoreThreadTimeOut(config.isAllowCoreThreadTimeOut());
        threadPoolExecutorFactoryBean.setExposeUnconfigurableExecutor(config.isExposeUnconfigurableExecutor());
        threadPoolExecutorFactoryBean.setKeepAliveSeconds(config.getKeepAliveSeconds());
        threadPoolExecutorFactoryBean.setQueueCapacity(config.getQueueCapacity());

        return threadPoolExecutorFactoryBean;
    }

    @Bean
    public ApplicationContextListener applicationContextListener() {
        return new ApplicationContextListener();
    }

    @Bean
    public EventExecutor eventExecutor(ExecutorService executorService) throws Exception {
        return new EventExecutor(executorService);
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
    @SuppressWarnings("unchecked")
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer placeholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        placeholderConfigurer.setFileEncoding("UTF-8");

        Map properties = Env.getEnvs();
        properties.putAll(Env.getSystemEnvs());
        properties.putAll(Ini.getInisProperties());
        properties.putAll(ProfileInitializer.getAllProfiles());

        placeholderConfigurer.setProperties(MapUtils.toProperties(properties));

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
    @ConditionalOnClass(MessagingException.class)
    @ComponentScan("lodsve.core.email")
    public static class LodsveMail {
    }
}

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lodsve.core.configuration;

import lodsve.core.autoproperties.Env;
import lodsve.core.autoproperties.Ini;
import lodsve.core.autoproperties.message.DefaultResourceBundleMessageSource;
import lodsve.core.autoproperties.message.ResourceBundleHolder;
import lodsve.core.autoproperties.profile.ProfileInitializer;
import lodsve.core.autoproperties.relaxedbind.annotations.EnableConfigurationProperties;
import lodsve.core.condition.ConditionalOnClass;
import lodsve.core.configuration.properties.ApplicationProperties;
import lodsve.core.configuration.properties.ThreadConfig;
import lodsve.core.context.ApplicationContextListener;
import lodsve.core.event.EventExecutor;
import lodsve.core.event.EventPublisher;
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
@EnableConfigurationProperties(ApplicationProperties.class)
@ComponentScan({
    "lodsve.core.exception",
    "lodsve.core.autoproperties"
})
public class LodsveCoreConfiguration {
    private final ApplicationProperties applicationProperties;

    public LodsveCoreConfiguration(ObjectProvider<ApplicationProperties> applicationProperties) {
        this.applicationProperties = applicationProperties.getIfAvailable();
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
    public DefaultResourceBundleMessageSource messageSource(ResourceBundleHolder resourceBundleHolder) {
        DefaultResourceBundleMessageSource messageSource = new DefaultResourceBundleMessageSource();
        messageSource.setAlwaysUseMessageFormat(true);
        messageSource.setResourceBundleHolder(resourceBundleHolder);

        return messageSource;
    }

    @Configuration
    @ConditionalOnClass(MessagingException.class)
    @ComponentScan("lodsve.core.mail")
    public static class LodsveMail {
    }
}

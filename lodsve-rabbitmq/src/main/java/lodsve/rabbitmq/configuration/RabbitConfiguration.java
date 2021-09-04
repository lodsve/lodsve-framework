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
package lodsve.rabbitmq.configuration;

import com.rabbitmq.client.Channel;
import lodsve.core.autoproperties.relaxedbind.annotations.EnableConfigurationProperties;
import lodsve.core.condition.ConditionalOnClass;
import lodsve.core.condition.ConditionalOnMissingBean;
import lodsve.rabbitmq.core.DefaultRabbitErrorHandler;
import lodsve.rabbitmq.core.RabbitErrorHandler;
import lodsve.rabbitmq.core.RabbitJackson2JsonMessageConverter;
import org.springframework.amqp.rabbit.annotation.RabbitListenerAnnotationBeanPostProcessor;
import org.springframework.amqp.rabbit.config.RabbitListenerConfigUtils;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * rabbit mq base configuration.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016-01-15 12:00
 */
@Configuration
@ConditionalOnClass({RabbitTemplate.class, Channel.class})
@EnableConfigurationProperties(RabbitProperties.class)
public class RabbitConfiguration {
    /**
     * rabbit 注解的配置
     */
    @Configuration
    public static class AnnotationAmqpConfiguration {
        @Bean(name = RabbitListenerConfigUtils.RABBIT_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME)
        @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
        public RabbitListenerAnnotationBeanPostProcessor rabbitListenerAnnotationProcessor() {
            return new RabbitListenerAnnotationBeanPostProcessor();
        }

        @Bean(name = RabbitListenerConfigUtils.RABBIT_LISTENER_ENDPOINT_REGISTRY_BEAN_NAME)
        public RabbitListenerEndpointRegistry defaultRabbitListenerEndpointRegistry() {
            return new RabbitListenerEndpointRegistry();
        }

        @Bean
        @ConditionalOnMissingBean(MessageConverter.class)
        public MessageConverter messageConverter() {
            // 原生的Jackson2JsonMessageConverter对泛型处理有些问题,所以我继承了这个类重写了反序列化生成泛型JavaType的方法,如果以后这段代码可以,将原生的Jackson2JsonMessageConverter返回即可
            return new RabbitJackson2JsonMessageConverter();
        }

        @Bean
        @ConditionalOnMissingBean(RabbitErrorHandler.class)
        public RabbitErrorHandler errorHandler() {
            return new DefaultRabbitErrorHandler();
        }
    }

    /**
     * rabbit连接池配置，当项目中配置了连接池，此处不起作用
     */
    @Configuration
    @ConditionalOnMissingBean(ConnectionFactory.class)
    public static class RabbitConnectionConfiguration {
        @Bean
        public CachingConnectionFactory connectionFactory(RabbitProperties rabbitProperties) {
            CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
            connectionFactory.setUsername(rabbitProperties.getUsername());
            connectionFactory.setPassword(rabbitProperties.getPassword());
            connectionFactory.setAddresses(rabbitProperties.getAddress());

            return connectionFactory;
        }
    }

    @Configuration
    @Import({RabbitConnectionConfiguration.class, AnnotationAmqpConfiguration.class})
    public static class RabbitTemplateConfiguration {
        private final CachingConnectionFactory connectionFactory;
        private final MessageConverter messageConverter;

        public RabbitTemplateConfiguration(ObjectProvider<CachingConnectionFactory> connectionFactory, ObjectProvider<MessageConverter> messageConverter) {
            this.connectionFactory = connectionFactory.getIfAvailable();
            this.messageConverter = messageConverter.getIfAvailable();
        }

        @Bean
        public RabbitTemplate rabbitTemplate() {
            RabbitTemplate template = new RabbitTemplate(connectionFactory);
            RetryTemplate retryTemplate = new RetryTemplate();
            ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
            backOffPolicy.setInitialInterval(500);
            backOffPolicy.setMultiplier(10.0);
            backOffPolicy.setMaxInterval(10000);
            retryTemplate.setBackOffPolicy(backOffPolicy);
            template.setRetryTemplate(retryTemplate);
            template.setMessageConverter(messageConverter);
            return template;
        }

        @Bean
        public RabbitAdmin rabbitAdmin() {
            return new RabbitAdmin(connectionFactory);
        }
    }

    @Configuration
    @Import({RabbitConnectionConfiguration.class, AnnotationAmqpConfiguration.class})
    protected static class RabbitListenerContainerConfiguration {
        private final CachingConnectionFactory connectionFactory;
        private final RabbitProperties rabbitProperties;
        private final RabbitErrorHandler errorHandler;

        public RabbitListenerContainerConfiguration(ObjectProvider<CachingConnectionFactory> connectionFactory, ObjectProvider<RabbitProperties> rabbitProperties, ObjectProvider<RabbitErrorHandler> errorHandler) {
            this.connectionFactory = connectionFactory.getIfAvailable();
            this.rabbitProperties = rabbitProperties.getIfAvailable();
            this.errorHandler = errorHandler.getIfAvailable();
        }

        @Bean
        @ConditionalOnMissingBean(name = "rabbitListenerContainerFactory")
        public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(MessageConverter messageConverter) {
            SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
            factory.setConnectionFactory(connectionFactory);
            factory.setMessageConverter(messageConverter);
            factory.setDefaultRequeueRejected(rabbitProperties.getRequeueRejected());
            factory.setErrorHandler(errorHandler);

            return factory;
        }
    }
}

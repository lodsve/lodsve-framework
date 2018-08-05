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

package lodsve.amqp.configs;

import com.rabbitmq.client.Channel;
import lodsve.amqp.core.AmqpErrorHandler;
import lodsve.amqp.core.DefaultAmqpErrorHandler;
import lodsve.amqp.core.LodsveJackson2JsonMessageConverter;
import lodsve.core.condition.ConditionalOnClass;
import lodsve.core.condition.ConditionalOnMissingBean;
import lodsve.core.properties.relaxedbind.annotations.EnableConfigurationProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListenerAnnotationBeanPostProcessor;
import org.springframework.amqp.rabbit.config.RabbitListenerConfigUtils;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
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
            return new LodsveJackson2JsonMessageConverter();
        }

        @Bean
        @ConditionalOnMissingBean(AmqpErrorHandler.class)
        public AmqpErrorHandler errorHandler() {
            return new DefaultAmqpErrorHandler();
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
        @Autowired
        private CachingConnectionFactory connectionFactory;
        @Autowired
        private MessageConverter messageConverter;

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
        @Autowired
        private CachingConnectionFactory connectionFactory;
        @Autowired
        private RabbitProperties rabbitProperties;
        @Autowired
        private AmqpErrorHandler errorHandler;

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

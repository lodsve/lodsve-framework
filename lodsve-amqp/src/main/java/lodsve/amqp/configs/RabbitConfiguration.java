package lodsve.amqp.configs;

import lodsve.amqp.core.DefaultErrorHandler;
import lodsve.amqp.core.LodsveJackson2JsonMessageConverter;
import lodsve.amqp.core.RabbitUtils;
import lodsve.core.condition.ConditionalOnMissingBean;
import lodsve.core.condition.ConditionalOnSingleCandidate;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * rabbit mq base configuration.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2016-01-15 12:00
 */
@Configuration
public class RabbitConfiguration {

    @Bean
    public RabbitUtils rabbitUtils() {
        return new RabbitUtils();
    }

    @Configuration
    @ConditionalOnMissingBean(ConnectionFactory.class)
    protected static class RabbitConnectionFactoryConfiguration {
        @Bean(name = RabbitUtils.CONNECTION_FACTORY_BEAN_NAME)
        public CachingConnectionFactory connectionFactory(RabbitProperties rabbitProperties) {
            CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
            connectionFactory.setUsername(rabbitProperties.getUsername());
            connectionFactory.setPassword(rabbitProperties.getPassword());
            connectionFactory.setAddresses(rabbitProperties.getAddress());

            return connectionFactory;
        }
    }

    @Configuration
    @Import(RabbitConnectionFactoryConfiguration.class)
    protected static class RabbitListenerContainerConfiguration {
        @Autowired
        private CachingConnectionFactory connectionFactory;
        @Autowired
        private MessageConverter messageConverter;
        @Autowired
        private RabbitProperties rabbitProperties;

        @Bean
        @ConditionalOnMissingBean(name = "rabbitListenerContainerFactory")
        public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
            SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
            factory.setConnectionFactory(connectionFactory);
            factory.setMessageConverter(messageConverter);
            factory.setDefaultRequeueRejected(rabbitProperties.getDefaultRequeueRejected());
            factory.setErrorHandler(new DefaultErrorHandler());

            return factory;
        }
    }

    @Configuration
    @Import(RabbitConnectionFactoryConfiguration.class)
    protected static class RabbitTemplateConfiguration {
        @Autowired
        private CachingConnectionFactory connectionFactory;
        @Autowired
        private MessageConverter messageConverter;

        @Bean
        @ConditionalOnSingleCandidate(ConnectionFactory.class)
        @ConditionalOnMissingBean(RabbitTemplate.class)
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
        @ConditionalOnSingleCandidate(ConnectionFactory.class)
        @ConditionalOnMissingBean(AmqpAdmin.class)
        public RabbitAdmin rabbitAdmin() {
            return new RabbitAdmin(connectionFactory);
        }
    }

    @Configuration
    protected static class ExtRabbitConfiguration {
        @Bean(name = RabbitUtils.DEFAULT_EXCHANGE_BEAN_NAME)
        public DirectExchange defaultDirectExchange(RabbitProperties rabbitProperties) {
            return new DirectExchange(rabbitProperties.getDefaultExchange());
        }

        @Bean(name = RabbitUtils.DEFAULT_MESSAGE_CONVERTER_BEAN_NAME)
        public MessageConverter messageConverter() {
            // 原生的Jackson2JsonMessageConverter对泛型处理有些问题,所以我继承了这个类重写了反序列化生成泛型JavaType的方法,如果以后这段代码可以,将原生的Jackson2JsonMessageConverter返回即可
            return new LodsveJackson2JsonMessageConverter();
        }
    }
}

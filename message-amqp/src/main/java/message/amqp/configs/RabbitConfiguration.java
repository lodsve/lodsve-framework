package message.amqp.configs;

import message.amqp.core.CosmosJackson2JsonMessageConverter;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * rabbit mq base configuration.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-01-15 12:00
 */
@Configuration
public class RabbitConfiguration {
    @Autowired
    private RabbitProperties rabbitProperties;

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        connectionFactory.setAddresses(rabbitProperties.getAddress());

        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        RetryTemplate retryTemplate = new RetryTemplate();
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(500);
        backOffPolicy.setMultiplier(10.0);
        backOffPolicy.setMaxInterval(10000);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        template.setRetryTemplate(retryTemplate);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public DirectExchange defaultDirectExchange() {
        return new DirectExchange(rabbitProperties.getExchange());
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        // 原生的Jackson2JsonMessageConverter对泛型处理有些问题,所以我继承了这个类重写了反序列化生成泛型JavaType的方法,如果以后这段代码可以,将原生的Jackson2JsonMessageConverter返回即可
        return new CosmosJackson2JsonMessageConverter();
    }
}

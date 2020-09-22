/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lodsve.rabbitmq.annotations;

import lodsve.core.autoproperties.relaxedbind.RelaxedBindFactory;
import lodsve.core.bean.BeanRegisterUtils;
import lodsve.rabbitmq.binding.DirectQueueBinding;
import lodsve.rabbitmq.binding.FanoutQueueBinding;
import lodsve.rabbitmq.binding.TopicQueueBinding;
import lodsve.rabbitmq.configuration.ExchangeType;
import lodsve.rabbitmq.configuration.QueueConfig;
import lodsve.rabbitmq.configuration.RabbitProperties;
import org.apache.commons.collections.MapUtils;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册amqp的相关queue配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-08-02 14:32
 */
public class RabbitBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    private final RabbitProperties rabbitProperties;
    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>(16);

    public RabbitBeanDefinitionRegistrar() {
        rabbitProperties = new RelaxedBindFactory.Builder<>(RabbitProperties.class).build();
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, QueueConfig> queues = rabbitProperties.getQueues();

        if (MapUtils.isEmpty(queues)) {
            return;
        }

        for (String queueName : queues.keySet()) {
            QueueConfig config = queues.get(queueName);

            // 1. 创建queue
            createQueue(queueName, config.isDurable(), config.isExclusive(), config.isAutoDelete());
            // 2. 创建binding
            createBinding(queueName, config);
        }

        BeanRegisterUtils.registerBeans(beanDefinitionMap, registry);
    }

    private void createBinding(String queueName, QueueConfig config) {
        ExchangeType type = config.getExchangeType();
        Class<?> bindingRawClass;
        Class<?> exchangeRawClass;
        switch (type) {
            case TOPIC:
                bindingRawClass = TopicQueueBinding.class;
                exchangeRawClass = TopicExchange.class;
                break;
            case DIRECT:
                bindingRawClass = DirectQueueBinding.class;
                exchangeRawClass = DirectExchange.class;
                break;
            case FANOUT:
                bindingRawClass = FanoutQueueBinding.class;
                exchangeRawClass = FanoutExchange.class;
                break;
            case HEADERS:
                throw new RuntimeException("Lodsve-Framework not support HeadersExchange now!");
            default:
                bindingRawClass = null;
                exchangeRawClass = null;
        }

        BeanDefinitionBuilder binding = BeanDefinitionBuilder.genericBeanDefinition(bindingRawClass);
        binding.addConstructorArgReference(config.getExchangeName());
        binding.addConstructorArgReference(queueName);
        if (ExchangeType.FANOUT != type) {
            binding.addConstructorArgValue(config.getRoutingKey());
        }

        beanDefinitionMap.put(config.getExchangeName() + "_queue_binding", binding.getBeanDefinition());

        // 3. 创建exchange
        if (!beanDefinitionMap.containsKey(config.getExchangeName())) {
            BeanDefinitionBuilder exchange = BeanDefinitionBuilder.genericBeanDefinition(exchangeRawClass);
            exchange.addConstructorArgValue(config.getExchangeName());
            beanDefinitionMap.put(config.getExchangeName(), exchange.getBeanDefinition());
        }

    }

    private void createQueue(String queueName, boolean durable, boolean exclusive, boolean autoDelete) {
        BeanDefinitionBuilder queue = BeanDefinitionBuilder.genericBeanDefinition(Queue.class);
        queue.addConstructorArgValue(queueName);
        queue.addConstructorArgValue(durable);
        queue.addConstructorArgValue(exclusive);
        queue.addConstructorArgValue(autoDelete);

        beanDefinitionMap.put(queueName, queue.getBeanDefinition());
    }
}

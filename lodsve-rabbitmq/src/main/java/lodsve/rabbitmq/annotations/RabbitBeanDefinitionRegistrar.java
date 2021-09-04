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

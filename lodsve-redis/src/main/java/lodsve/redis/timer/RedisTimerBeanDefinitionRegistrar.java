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
package lodsve.redis.timer;

import lodsve.core.bean.BeanRegisterUtils;
import lodsve.core.utils.StringUtils;
import lodsve.redis.core.annotations.EnableRedis;
import lodsve.redis.exception.RedisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * redis timer 注册.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017/12/17 上午12:20
 */
public class RedisTimerBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    private static final Logger logger = LoggerFactory.getLogger(RedisTimerBeanDefinitionRegistrar.class);

    private static final String TIMER_NAME_ATTRIBUTE_NAME = "timer";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableRedis.class.getName(), false));
        String timer = attributes.getString(TIMER_NAME_ATTRIBUTE_NAME);

        if (StringUtils.isBlank(timer)) {
            if (logger.isDebugEnabled()) {
                logger.debug("redis timer is disabled!");
            }
            return;
        }

        if (!registry.containsBeanDefinition(timer)) {
            throw new RedisException(103003, "can't find dataSource named '{}' for Redis Timer!", timer);
        }

        Map<String, BeanDefinition> beanDefinitions = new HashMap<>(5);
        // RedisTemplate
        BeanDefinitionBuilder redisTimerRedisTemplate = BeanDefinitionBuilder.genericBeanDefinition(RedisTemplate.class);
        redisTimerRedisTemplate.addPropertyReference("connectionFactory", timer);
        redisTimerRedisTemplate.addPropertyValue("keySerializer", new StringRedisSerializer());
        redisTimerRedisTemplate.addPropertyValue("valueSerializer", new StringRedisSerializer());
        beanDefinitions.put("redisTimerRedisTemplate", redisTimerRedisTemplate.getBeanDefinition());

        //RedisTimerListener
        BeanDefinitionBuilder redisTimerListener = BeanDefinitionBuilder.genericBeanDefinition(RedisTimerListener.class);
        redisTimerListener.addConstructorArgReference("redisTimerRedisTemplate");
        beanDefinitions.put("redisTimerListener", redisTimerListener.getBeanDefinition());

        // RedisMessageListenerContainer
        BeanDefinitionBuilder redisTimerRedisMessageListenerContainer = BeanDefinitionBuilder.genericBeanDefinition(RedisTimerMessageListenerContainer.class);
        redisTimerRedisMessageListenerContainer.addConstructorArgReference(timer);
        redisTimerRedisMessageListenerContainer.addConstructorArgReference("redisTimerListener");
        beanDefinitions.put("redisTimerRedisMessageListenerContainer", redisTimerRedisMessageListenerContainer.getBeanDefinition());

        // RedisEventListener
        BeanDefinitionBuilder redisEventListener = BeanDefinitionBuilder.genericBeanDefinition(RedisEventListener.class);
        beanDefinitions.put("redisEventListener", redisEventListener.getBeanDefinition());

        // RedisEventUtils
        BeanDefinitionBuilder redisEventUtils = BeanDefinitionBuilder.genericBeanDefinition(RedisEventUtils.class);
        beanDefinitions.put("redisEventUtils", redisEventUtils.getBeanDefinition());

        BeanRegisterUtils.registerBeans(beanDefinitions, registry);
    }
}

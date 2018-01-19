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
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/12/17 上午12:20
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

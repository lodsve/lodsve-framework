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
package lodsve.core.condition;

import lodsve.core.autoproperties.Env;
import lodsve.core.autoproperties.relaxedbind.RelaxedBindFactory;
import lodsve.core.utils.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.Assert;

/**
 * conditional property是否匹配给定的值.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016/12/8 下午5:56
 */
@Order(Ordered.LOWEST_PRECEDENCE)
public class OnPropertyCondition extends SpringBootCondition implements ConfigurationCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        if (!metadata.isAnnotated(ConditionalOnProperty.class.getName())) {
            return ConditionOutcome.match(StringUtils.EMPTY);
        }

        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(ConditionalOnProperty.class.getName(), false));
        Assert.notNull(attributes, "AnnotationAttributes must be non-null!");

        String key = attributes.getString("key");
        String value = attributes.getString("value");
        Class<?> clazz = attributes.getClass("clazz");
        boolean notNull = attributes.getBoolean("notNull");

        String realValue;
        if (Object.class.equals(clazz)) {
            realValue = Env.getString(key);
        } else {
            Object obj = new RelaxedBindFactory.Builder<>(clazz).build();
            BeanWrapper beanWrapper = new BeanWrapperImpl(obj);

            Object keyValue = beanWrapper.getPropertyValue(key);
            realValue = keyValue != null ? keyValue.toString() : "";
        }

        if (notNull) {
            // 判断是否为空，直接返回取到的值是否为null即可
            boolean isMatch = StringUtils.isNotBlank(realValue);
            return new ConditionOutcome(isMatch, String.format("@ConditionalOnProperties's key: [%s] is %s!", key, isMatch ? "not null" : "null"));
        }


        if (!value.equals(realValue)) {
            return ConditionOutcome.noMatch(String.format("@ConditionalOnProperties's key:[%s], found value:[%s], not match given value:[%s]!", key, realValue, value));
        }

        return ConditionOutcome.match("@ConditionalOnProperties's key:[%s], found value:[%s], given value:[%s], matched!");
    }

    @Override
    public ConfigurationPhase getConfigurationPhase() {
        return ConfigurationPhase.REGISTER_BEAN;
    }
}

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

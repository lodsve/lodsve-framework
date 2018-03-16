package lodsve.core.condition;

import lodsve.core.properties.Env;
import lodsve.core.properties.relaxedbind.RelaxedBindFactory;
import lodsve.core.utils.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;

import java.util.Map;

/**
 * conditional property是否匹配给定的值.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2016/12/8 下午5:56
 */
@Order(Ordered.LOWEST_PRECEDENCE)
public class OnPropertyCondition extends SpringBootCondition implements ConfigurationCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        if (!metadata.isAnnotated(ConditionalOnProperty.class.getName())) {
            return ConditionOutcome.match(StringUtils.EMPTY);
        }

        Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnProperty.class.getName());
        String key = attributes.get("key").toString();
        String value = attributes.get("value").toString();
        String clazz = attributes.get("clazz").toString();

        String realValue;
        if (Object.class.getName().equals(clazz)) {
            realValue = Env.getString(key);
        } else {
            try {
                Class<?> c = ClassUtils.forName(clazz, OnPropertyCondition.class.getClassLoader());
                Object obj = new RelaxedBindFactory.Builder<>(c).build();
                BeanWrapper beanWrapper = new BeanWrapperImpl(obj);

                Object keyValue = beanWrapper.getPropertyValue(key);
                realValue = keyValue != null ? keyValue.toString() : "";
            } catch (ClassNotFoundException e) {
                return ConditionOutcome.noMatch(String.format("@ConditionalOnProperties's key:[%s], found value:[%s], not match given value:[%s]!", key, "", value));
            }
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

package lodsve.core.condition;

import lodsve.core.config.SystemConfig;
import lodsve.core.utils.StringUtils;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * conditional property是否匹配给定的值.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2016/12/8 下午5:56
 */
@Order(Ordered.LOWEST_PRECEDENCE)
public class OnPropertyConditional extends SpringBootCondition implements ConfigurationCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        if (!metadata.isAnnotated(ConditionalOnProperty.class.getName())) {
            return ConditionOutcome.match(StringUtils.EMPTY);
        }

        Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnProperty.class.getName());
        String key = attributes.get("key").toString();
        String value = attributes.get("value").toString();

        String realValue = SystemConfig.getString(key);

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

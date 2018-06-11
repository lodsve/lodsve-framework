package lodsve.core.condition;

import lodsve.core.properties.Env;
import lodsve.core.utils.StringUtils;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 是否是开发模式.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016/12/12 下午3:00
 */
@Order(Ordered.LOWEST_PRECEDENCE)
public class OnDevelopmentConditional extends SpringBootCondition implements ConfigurationCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        if (!metadata.isAnnotated(ConditionalOnDevelopment.class.getName())) {
            return ConditionOutcome.match(StringUtils.EMPTY);
        }

        Boolean isDevMode = Env.getBoolean("application.devMode", null);
        if (null == isDevMode) {
            isDevMode = Env.getBoolean("application.dev-mode", null);
        }

        isDevMode = null == isDevMode ? false : isDevMode;

        if (!isDevMode) {
            return ConditionOutcome.noMatch("this is not dev-mode!");
        }

        return ConditionOutcome.match("this is dev-mode!");
    }

    @Override
    public ConfigurationPhase getConfigurationPhase() {
        return ConfigurationPhase.REGISTER_BEAN;
    }
}

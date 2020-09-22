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

        isDevMode = null != isDevMode && isDevMode;

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

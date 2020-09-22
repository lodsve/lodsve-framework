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

import lodsve.core.condition.rule.Rule;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link org.springframework.context.annotation.Condition} that checks for rules...
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017/12/6 14:11
 * @see ConditionalOnRules
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 20)
public class OnRuleCondition extends SpringBootCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(ConditionalOnRules.class.getName(), true);
        List<Object> rules = attributes.get("rules");
        boolean allRulesPassed = Boolean.valueOf(String.valueOf(attributes.get("allRulesPassed")));

        List<String> ruleNames = new ArrayList<>();
        for (Object value : rules) {
            for (Object item : (Object[]) value) {
                ruleNames.add((String) item);
            }
        }

        boolean result = false;
        for (String name : ruleNames) {
            Class<?> clazz;
            try {
                clazz = ClassUtils.forName(name, Thread.currentThread().getContextClassLoader());
            } catch (ClassNotFoundException e) {
                continue;
            }

            Rule rule = (Rule) BeanUtils.instantiateClass(clazz);
            if (rule == null) {
                continue;
            }

            result = rule.match(context);
            if (!allRulesPassed && result) {
                // allPass == false 只要有一个结果为true，则返回，并且condition成立
                result = true;
                break;
            }
            if (allRulesPassed && !result) {
                // allPass == true 只要有一个结果为false，则返回，并且condition不成立
                result = false;
                break;
            }
        }

        if (result) {
            return ConditionOutcome.match(ConditionMessage.forCondition(ConditionalOnRules.class).notAvailable("rule passed!"));
        } else {
            return ConditionOutcome.noMatch(ConditionMessage.forCondition(ConditionalOnRules.class).notAvailable("rule not passed!"));
        }
    }
}

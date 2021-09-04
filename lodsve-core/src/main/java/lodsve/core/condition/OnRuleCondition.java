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

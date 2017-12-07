/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lodsve.core.condition;

import lodsve.core.condition.rule.Rule;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link org.springframework.context.annotation.Conditional} that only matches when all the rules return true
 * application context.
 *
 * @author Dave Syer
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnRuleCondition.class)
public @interface ConditionalOnRules {
    /**
     * the rules, must extends from {@link lodsve.core.condition.rule.Rule}
     *
     * @return rules
     */
    Class<? extends Rule>[] rules();

    /**
     * if true, all the rules must return true, then conditional is true!
     * if false, one of the rules return true, then conditional is true!
     *
     * @return true/false
     */
    boolean allRulesPassed() default true;
}

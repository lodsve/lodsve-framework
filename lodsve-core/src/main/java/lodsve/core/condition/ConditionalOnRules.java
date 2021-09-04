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
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * {@link org.springframework.context.annotation.Conditional} that only matches when all the rules return true
 * application context.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017/12/6 14:10
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

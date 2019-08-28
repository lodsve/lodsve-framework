/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

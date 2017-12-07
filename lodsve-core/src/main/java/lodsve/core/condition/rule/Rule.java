package lodsve.core.condition.rule;

import org.springframework.context.annotation.ConditionContext;

/**
 * rule.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2017/12/6 14:10
 * @see lodsve.core.condition.ConditionalOnRules
 */
public interface Rule {
    /**
     * match
     *
     * @param context Condition Context
     * @return true/false
     */
    boolean match(ConditionContext context);
}

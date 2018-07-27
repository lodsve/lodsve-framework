package lodsve.core.condition.rule;

import org.springframework.context.annotation.ConditionContext;

/**
 * rule.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017/12/6 14:10
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

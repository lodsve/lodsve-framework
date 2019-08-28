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

import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * A Condition that evaluates a SpEL expression.
 *
 * @author Dave Syer
 * @see ConditionalOnExpression
 */
@Order(Ordered.LOWEST_PRECEDENCE - 20)
class OnExpressionCondition extends SpringBootCondition {

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context,
			AnnotatedTypeMetadata metadata) {
		String expression = (String) metadata
				.getAnnotationAttributes(ConditionalOnExpression.class.getName())
				.get("value");
		expression = wrapIfNecessary(expression);
		String rawExpression = expression;
		expression = context.getEnvironment().resolvePlaceholders(expression);
		ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
		BeanExpressionResolver resolver = (beanFactory != null)
				? beanFactory.getBeanExpressionResolver() : null;
		BeanExpressionContext expressionContext = (beanFactory != null)
				? new BeanExpressionContext(beanFactory, null) : null;
		if (resolver == null) {
			resolver = new StandardBeanExpressionResolver();
		}
		boolean result = (Boolean) resolver.evaluate(expression, expressionContext);
		return new ConditionOutcome(result, ConditionMessage
				.forCondition(ConditionalOnExpression.class, "(" + rawExpression + ")")
				.resultedIn(result));
	}

	/**
	 * Allow user to provide bare expression with no '#{}' wrapper.
	 * @param expression source expression
	 * @return wrapped expression
	 */
	private String wrapIfNecessary(String expression) {
		if (!expression.startsWith("#{")) {
			return "#{" + expression + "}";
		}
		return expression;
	}

}

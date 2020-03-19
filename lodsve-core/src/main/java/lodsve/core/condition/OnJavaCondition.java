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

import lodsve.core.condition.ConditionalOnJava.JavaVersion;
import lodsve.core.condition.ConditionalOnJava.Range;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * {@link Condition} that checks for a required version of Java.
 *
 * @author Oliver Gierke
 * @author Phillip Webb
 * @see ConditionalOnJava
 * @since 1.1.0
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 20)
class OnJavaCondition extends SpringBootCondition {

	private static final JavaVersion JVM_VERSION = JavaVersion.getJavaVersion();

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context,
			AnnotatedTypeMetadata metadata) {
		Map<String, Object> attributes = metadata
				.getAnnotationAttributes(ConditionalOnJava.class.getName());
		Range range = (Range) attributes.get("range");
		JavaVersion version = (JavaVersion) attributes.get("value");
		return getMatchOutcome(range, JVM_VERSION, version);
	}

	protected ConditionOutcome getMatchOutcome(Range range, JavaVersion runningVersion,
			JavaVersion version) {
		boolean match = runningVersion.isWithin(range, version);
		String expected = String.format(
				range == Range.EQUAL_OR_NEWER ? "(%s or newer)" : "(older than %s)",
				version);
		ConditionMessage message = ConditionMessage
				.forCondition(ConditionalOnJava.class, expected)
				.foundExactly(runningVersion);
		return new ConditionOutcome(match, message);
	}

}

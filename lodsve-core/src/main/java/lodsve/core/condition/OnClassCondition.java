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

import lodsve.core.condition.ConditionMessage.Style;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * {@link Condition} that checks for the
 * presence or absence of specific classes.
 *
 * @author Phillip Webb
 * @see ConditionalOnClass
 * @see ConditionalOnMissingClass
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
class OnClassCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context,
                                            AnnotatedTypeMetadata metadata) {
        ClassLoader classLoader = context.getClassLoader();
        ConditionMessage matchMessage = ConditionMessage.empty();
        List<String> onClasses = getCandidates(metadata, ConditionalOnClass.class);
        if (onClasses != null) {
            List<String> missing = getMatches(onClasses, MatchType.MISSING, classLoader);
            if (!missing.isEmpty()) {
                return ConditionOutcome
                        .noMatch(ConditionMessage.forCondition(ConditionalOnClass.class)
                                .didNotFind("required class", "required classes")
                                .items(Style.QUOTE, missing));
            }
            matchMessage = matchMessage.andCondition(ConditionalOnClass.class)
                    .found("required class", "required classes").items(Style.QUOTE,
                            getMatches(onClasses, MatchType.PRESENT, classLoader));
        }
        List<String> onMissingClasses = getCandidates(metadata,
                ConditionalOnMissingClass.class);
        if (onMissingClasses != null) {
            List<String> present = getMatches(onMissingClasses, MatchType.PRESENT,
                    classLoader);
            if (!present.isEmpty()) {
                return ConditionOutcome.noMatch(
                        ConditionMessage.forCondition(ConditionalOnMissingClass.class)
                                .found("unwanted class", "unwanted classes")
                                .items(Style.QUOTE, present));
            }
            matchMessage = matchMessage.andCondition(ConditionalOnMissingClass.class)
                    .didNotFind("unwanted class", "unwanted classes").items(Style.QUOTE,
                            getMatches(onMissingClasses, MatchType.MISSING, classLoader));
        }
        return ConditionOutcome.match(matchMessage);
    }

    private List<String> getCandidates(AnnotatedTypeMetadata metadata,
                                       Class<?> annotationType) {
        MultiValueMap<String, Object> attributes = metadata
                .getAllAnnotationAttributes(annotationType.getName(), true);
        List<String> candidates = new ArrayList<String>();
        if (attributes == null) {
            return Collections.emptyList();
        }
        addAll(candidates, attributes.get("value"));
        addAll(candidates, attributes.get("name"));
        return candidates;
    }

    private void addAll(List<String> list, List<Object> itemsToAdd) {
        if (itemsToAdd != null) {
            for (Object item : itemsToAdd) {
                Collections.addAll(list, (String[]) item);
            }
        }
    }

    private List<String> getMatches(Collection<String> candidates, MatchType matchType,
                                    ClassLoader classLoader) {
        List<String> matches = new ArrayList<String>(candidates.size());
        for (String candidate : candidates) {
            if (matchType.matches(candidate, classLoader)) {
                matches.add(candidate);
            }
        }
        return matches;
    }

    private enum MatchType {

        PRESENT {
            @Override
            public boolean matches(String className, ClassLoader classLoader) {
                return isPresent(className, classLoader);
            }

        },

        MISSING {
            @Override
            public boolean matches(String className, ClassLoader classLoader) {
                return !isPresent(className, classLoader);
            }

        };

        private static boolean isPresent(String className, ClassLoader classLoader) {
            if (classLoader == null) {
                classLoader = ClassUtils.getDefaultClassLoader();
            }
            try {
                forName(className, classLoader);
                return true;
            } catch (Throwable ex) {
                return false;
            }
        }

        private static Class<?> forName(String className, ClassLoader classLoader)
                throws ClassNotFoundException {
            if (classLoader != null) {
                return classLoader.loadClass(className);
            }
            return Class.forName(className);
        }

        public abstract boolean matches(String className, ClassLoader classLoader);

    }

    private interface OutcomesResolver {

        ConditionOutcome[] resolveOutcomes();

    }

}

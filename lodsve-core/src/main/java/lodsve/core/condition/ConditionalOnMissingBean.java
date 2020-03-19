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

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * {@link Conditional} that only matches when the specified bean classes and/or names are
 * not already contained in the {@link BeanFactory}.
 * <p>
 * When placed on a {@code @Bean} method, the bean class defaults to the return type of
 * the factory method:
 *
 * <pre class="code">
 * &#064;Configuration
 * public class MyAutoConfiguration {
 *
 *     &#064;ConditionalOnMissingBean
 *     &#064;Bean
 *     public MyService myService() {
 *         ...
 *     }
 *
 * }</pre>
 * <p>
 * In the sample above the condition will match if no bean of type {@code MyService} is
 * already contained in the {@link BeanFactory}.
 * <p>
 * The condition can only match the bean definitions that have been processed by the
 * application context so far and, as such, it is strongly recommended to use this
 * condition on auto-configuration classes only. If a candidate bean may be created by
 * another auto-configuration, make sure that the one using this condition runs after.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnBeanCondition.class)
public @interface ConditionalOnMissingBean {

    /**
     * The class type of bean that should be checked. The condition matches when each
     * class specified is missing in the {@link ApplicationContext}.
     *
     * @return the class types of beans to check
     */
    Class<?>[] value() default {};

    /**
     * The class type names of bean that should be checked. The condition matches when
     * each class specified is missing in the {@link ApplicationContext}.
     *
     * @return the class type names of beans to check
     */
    String[] type() default {};

    /**
     * The class type of beans that should be ignored when identifying matching beans.
     *
     * @return the class types of beans to ignore
     * @since 1.2.5
     */
    Class<?>[] ignored() default {};

    /**
     * The class type names of beans that should be ignored when identifying matching
     * beans.
     *
     * @return the class type names of beans to ignore
     * @since 1.2.5
     */
    String[] ignoredType() default {};

    /**
     * The annotation type decorating a bean that should be checked. The condition matches
     * when each annotation specified is missing from all beans in the
     * {@link ApplicationContext}.
     *
     * @return the class-level annotation types to check
     */
    Class<? extends Annotation>[] annotation() default {};

    /**
     * The names of beans to check. The condition matches when each bean name specified is
     * missing in the {@link ApplicationContext}.
     *
     * @return the name of beans to check
     */
    String[] name() default {};

    /**
     * Strategy to decide if the application context hierarchy (parent contexts) should be
     * considered.
     *
     * @return the search strategy
     */
    SearchStrategy search() default SearchStrategy.ALL;

}

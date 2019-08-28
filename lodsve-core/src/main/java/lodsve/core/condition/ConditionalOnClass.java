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

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * {@link Conditional} that only matches when the specified classes are on the classpath.
 *
 * @author Phillip Webb
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnClassCondition.class)
public @interface ConditionalOnClass {

    /**
     * The classes that must be present. Since this annotation is parsed by loading class
     * bytecode, it is safe to specify classes here that may ultimately not be on the
     * classpath, only if this annotation is directly on the affected component and
     * <b>not</b> if this annotation is used as a composed, meta-annotation. In order to
     * use this annotation as a meta-annotation, only use the {@link #name} attribute.
     *
     * @return the classes that must be present
     */
    Class<?>[] value() default {};

    /**
     * The classes names that must be present.
     *
     * @return the class names that must be present.
     */
    String[] name() default {};

}

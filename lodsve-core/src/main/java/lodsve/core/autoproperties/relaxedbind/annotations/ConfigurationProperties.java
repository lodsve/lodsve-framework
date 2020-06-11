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

package lodsve.core.autoproperties.relaxedbind.annotations;

import java.lang.annotation.*;

/**
 * 配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 15/12/30 上午9:28
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ConfigurationProperties {
    /**
     * The name prefix of the properties that are valid to bind to this object.
     *
     * @return the name prefix of the properties to bind
     */
    String prefix() default "";

    /**
     * Optionally provide explicit resource locations to bind to. By default the
     * configuration at these specified locations will be merged with the default
     * configuration.
     *
     * @return the path (or paths) of resources to bind to
     */
    String[] locations() default {};
}

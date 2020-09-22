/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lodsve.web.mvc.annotation;

import java.lang.annotation.*;

/**
 * 当前台传入类似于1,2,3,4,5之类的字符串，加上此注解，将会被解析成一个List，这个注解是一些配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2015-2-9 18:58
 * @see lodsve.web.mvc.annotation.resolver.ParseDataHandlerMethodArgumentResolver
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Parse {
    /**
     * 参数值在request中key，默认为参数名
     *
     * @return
     */
    String value() default "";

    /**
     * list中需要的泛型
     *
     * @return
     */
    Class<?> dest();

    /**
     * 是否必填
     *
     * @return
     */
    boolean required() default false;

    /**
     * 分隔符，默认为[,]
     *
     * @return
     */
    String delimiters() default ",";
}

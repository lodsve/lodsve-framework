/*
 * Copyright (C) 2018  Sun.Hao
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

package lodsve.web.springfox.annotations;

import lodsve.core.configuration.EnableLodsve;
import lodsve.web.springfox.config.SpringFoxConfiguration;
import org.springframework.context.annotation.Import;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;

import java.lang.annotation.*;

/**
 * 启用lodsve-mvc中得spring fox.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @date 16/3/24 上午9:28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@EnableLodsve
@Import({SpringFoxRegistrar.class, SpringFoxConfiguration.class})
public @interface EnableSpringFox {
    /**
     * url路径前缀
     *
     * @return 前缀
     */
    String prefix() default RelativePathProvider.ROOT;

    /**
     * 设置分组
     *
     * @return 分组
     */
    String[] groups() default Docket.DEFAULT_GROUP_NAME;
}

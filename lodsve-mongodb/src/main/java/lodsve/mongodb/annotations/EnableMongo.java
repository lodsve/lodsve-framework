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

package lodsve.mongodb.annotations;

import lodsve.core.configuration.EnableLodsve;
import lodsve.mongodb.config.MongoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用mongodb.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/1/21 下午10:14
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableLodsve
@Import({MongoConfiguration.class, MongoBeanDefinitionRegistrar.class})
public @interface EnableMongo {
    /**
     * 数据源名
     *
     * @return
     */
    String[] dataSource();

    /**
     * 含有{@link org.springframework.data.mongodb.core.mapping.Document }注解的dao类所在的包路径
     *
     * @return
     * @see org.springframework.data.mongodb.core.mapping.Document
     */
    String[] domainPackages() default {};

    /**
     * 含有{@link org.springframework.stereotype.Repository }注解的dao类所在的包路径
     *
     * @return
     * @see org.springframework.stereotype.Repository
     */
    String[] basePackages() default {};

    /**
     * Specifies which types are eligible for component scanning. Further narrows the set of candidate components from
     * everything in {@link #basePackages()} to everything in the base packages that matches the given filter or filters.
     */
    ComponentScan.Filter[] includeFilters() default {};

    /**
     * Specifies which types are not eligible for component scanning.
     */
    ComponentScan.Filter[] excludeFilters() default {};
}

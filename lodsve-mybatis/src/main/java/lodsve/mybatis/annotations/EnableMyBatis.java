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

package lodsve.mybatis.annotations;

import lodsve.core.configuration.EnableLodsve;
import lodsve.mybatis.configuration.MyBatisConfiguration;
import lodsve.mybatis.utils.DbType;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;

import java.lang.annotation.*;

/**
 * 通过注解的方式启动mybatis的配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/1/19 下午8:01
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableLodsve
@Import({MyBatisConfiguration.class, MybatisConfigsRegistrar.class})
public @interface EnableMyBatis {
    /**
     * Base packages to scan for MyBatis interfaces. Note that only interfaces
     * with at least one method will be registered; concrete classes will be
     * ignored.
     */
    String[] basePackages() default {};

    /**
     * 枚举类型所在包路径,可以多个
     *
     * @return 枚举类型所在包路径
     */
    String[] enumsLocations() default {};

    /**
     * This property specifies the annotation that the scanner will search for.
     * <p>
     * The scanner will register all interfaces in the base package that also have
     * the specified annotation.
     * <p>
     * Note this can be combined with markerInterface.
     */
    Class<? extends Annotation> annotationClass() default Repository.class;

    /**
     * 是否要匹配驼峰规则
     *
     * @return true/false
     */
    boolean mapUnderscoreToCamelCase() default true;

    /**
     * 所使用的数据库类型
     *
     * @return 数据库，默认MySQL
     */
    DbType type() default DbType.DB_MYSQL;
}

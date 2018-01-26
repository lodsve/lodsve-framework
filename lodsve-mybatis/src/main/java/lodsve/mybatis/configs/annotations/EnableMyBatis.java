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

package lodsve.mybatis.configs.annotations;

import lodsve.core.configuration.EnableLodsve;
import lodsve.mybatis.configs.MyBatisConfigurationSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 通过注解的方式启动mybatis的配置.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/1/19 下午8:01
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableLodsve
@Import(MyBatisConfigurationSelector.class)
public @interface EnableMyBatis {
    /**
     * 数据源名,多数据源,第一个数据源为默认值
     *
     * @return
     */
    String[] dataSource();

    /**
     * 是否支持事务
     *
     * @return
     */
    boolean supportTransaction() default false;

    /**
     * mybatis mapper文件
     *
     * @return resource
     */
    String[] mapperLocations() default "classpath*:/META-INF/mybatis/**/*Mapper.xml";

    /**
     * mybatis 配置文件
     *
     * @return resource
     */
    String configLocation() default "classpath:/META-INF/mybatis/mybatis.xml";

    /**
     * 含有{@link org.springframework.stereotype.Repository }注解的dao类所在的包路径,可以多个
     *
     * @return
     * @see org.springframework.stereotype.Repository
     */
    String[] basePackages() default {};

    /**
     * 枚举类型所在包路径,可以多个
     *
     * @return
     */
    String[] enumsLocations() default {};

    /**
     * 是否使用flyway
     *
     * @return
     */
    boolean useFlyway() default false;

    /**
     * flyway的脚本文件所在路径
     *
     * @return
     */
    String migration() default "META-INF/flyway";
}

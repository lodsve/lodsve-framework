/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

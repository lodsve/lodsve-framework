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
package lodsve.mongodb.annotations;

import lodsve.core.configuration.EnableLodsve;
import lodsve.mongodb.config.MongoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用mongodb.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/1/21 下午10:14
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

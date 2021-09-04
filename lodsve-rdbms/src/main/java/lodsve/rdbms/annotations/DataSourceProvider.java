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
package lodsve.rdbms.annotations;

import lodsve.core.configuration.EnableLodsve;
import lodsve.rdbms.configuration.DataSourceBeanDefinitionRegistrar;
import lodsve.rdbms.configuration.DataSourceTransactionManagementConfiguration;
import lodsve.rdbms.configuration.RdbmsConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 数据源提供者.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017/12/14 下午5:33
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({RdbmsConfiguration.class, DataSourceTransactionManagementConfiguration.class, DataSourceBeanDefinitionRegistrar.class})
@EnableLodsve
public @interface DataSourceProvider {
    /**
     * 选择的数据源
     *
     * @return 数据源名称
     */
    String[] dataSource();
}

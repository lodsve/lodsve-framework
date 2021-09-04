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
package lodsve.rdbms.properties;

import lodsve.core.autoproperties.relaxedbind.annotations.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * rdbms base properties,only support BasicDataSource and DruidDataSource.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016-1-27 09:20
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "lodsve.rdbms", locations = "${params.root}/framework/rdbms.properties")
public class RdbmsProperties {
    /**
     * 数据源类型
     */
    private String dataSourceClass = "org.apache.commons.dbcp.BasicDataSource";
    /**
     * 是否支持事务
     */
    private boolean supportTransaction = false;
    /**
     * 连接信息
     */
    private Map<String, PoolSetting> pool;
}

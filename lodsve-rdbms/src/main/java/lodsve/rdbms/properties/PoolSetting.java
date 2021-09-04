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

import lodsve.core.autoproperties.relaxedbind.annotations.Required;
import lombok.Getter;
import lombok.Setter;

/**
 * Pool Setting.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-4-25-0025 14:28
 */
@Setter
@Getter
public class PoolSetting {
    private String driverClassName = "com.mysql.jdbc.Driver";
    @Required
    private String url;
    @Required
    private String username;
    @Required
    private String password;
    private Integer initialSize = 10;
    private Integer maxActive = 100;
    private Integer minIdle = 20;
    private Integer maxWait = 60000;
    private Boolean removeAbandoned = true;
    private Integer removeAbandonedTimeout = 180;
    private Boolean testOnBorrow = true;
    private Boolean testOnReturn = true;
    private Boolean testWhileIdle = false;
    private String validationQuery = "select 1";
    private Integer maxIdle = 5;
}

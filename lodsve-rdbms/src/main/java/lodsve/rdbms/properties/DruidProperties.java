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

import com.alibaba.druid.wall.WallConfig;
import lodsve.core.autoproperties.relaxedbind.annotations.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * druid配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-2-8-0008 15:05
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "lodsve.druid", locations = "${params.root}/framework/druid.properties")
public class DruidProperties {
    private String filters = "stat\\,wall";
    private WallConfig wallConfig;
    private boolean enableMonitor = false;
    private String path = "/druid/*";
    private String resetEnable = "true";
    private String user;
    private String password;
    private String allow;
    private String deny;
    private String remoteAddress;
}

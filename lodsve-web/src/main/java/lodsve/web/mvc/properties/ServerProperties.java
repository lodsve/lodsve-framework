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
package lodsve.web.mvc.properties;

import lodsve.core.autoproperties.relaxedbind.annotations.ConfigurationProperties;
import lodsve.core.autoproperties.relaxedbind.annotations.Required;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 15/10/10 下午10:01
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "lodsve.server", locations = "${params.root}/framework/server.properties")
public class ServerProperties {
    /**
     * 前台URL
     */
    @Required
    private String frontEndUrl;
    /**
     * 服务端URL
     */
    @Required
    private String serverUrl;
    /**
     * debug配置
     */
    private DebugConfig debug;
    /**
     * 是否启用验证码
     */
    private boolean enableCaptcha = false;
    /**
     * 验证码在session中的key
     */
    private String captchaKey = "captchaKey";
    /**
     * 请求验证码的路径
     */
    private String path = "/captcha";
}

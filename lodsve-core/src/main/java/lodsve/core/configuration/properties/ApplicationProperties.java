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
package lodsve.core.configuration.properties;

import lodsve.core.autoproperties.relaxedbind.annotations.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016/12/12 下午3:05
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "application", locations = "${params.root}/framework/application.properties")
public class ApplicationProperties {
    /**
     * 开发模式
     */
    private boolean devMode = true;
    /**
     * 编码
     */
    private String encoding = "UTF-8";
    /**
     * whether the specified encoding is supposed to
     * override existing request and response encodings
     */
    private boolean forceEncoding = true;
    /**
     * banner配置
     */
    private BannerConfig banner;
    /**
     * 多线程配置
     */
    private ThreadConfig thread;
    /**
     * 控制台打印参数配置
     */
    private AnsiConfig ansi;
    /**
     * 日志配置文件
     */
    private LogbackConfig logback;
}

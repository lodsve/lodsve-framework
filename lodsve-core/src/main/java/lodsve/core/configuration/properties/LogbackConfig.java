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

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 日志文件配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
@Setter
@Getter
public class LogbackConfig {
    /**
     * logback配置文件
     */
    private String config;
    /**
     * logback日志文件路径
     */
    private String logFile;
    /**
     * 控制台打印格式化字符串
     */
    private String consoleLogPattern;
    /**
     * 打印到日志文件的格式化字符串
     */
    private String fileLogPattern;
    /**
     * 打印到日志文件的最大文件大小
     */
    private String fileLogMaxSize;
    /**
     * 打印到日志文件的最大个数
     */
    private Integer fileLogMaxHistory;
    /**
     * 日志级别配置 
     */
    private Map<String, String> level = Maps.newHashMap();
}

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
package lodsve.search.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * Solr Config.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-4-25-0025 14:47
 */
@Setter
@Getter
public class SolrConfig {
    /**
     * 高亮前缀
     */
    private String prefix = "<span style='color: red'>";
    /**
     * 高亮后缀
     */
    private String suffix = "</span>";
    /**
     * solr服务器地址
     */
    private String server;
    /**
     * solr 6.6.0使用的哪个core
     */
    private String core;
}

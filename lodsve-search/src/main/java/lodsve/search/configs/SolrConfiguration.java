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
package lodsve.search.configs;

import lodsve.core.autoproperties.relaxedbind.annotations.EnableConfigurationProperties;
import lodsve.core.condition.ConditionalOnMissingBean;
import lodsve.core.utils.StringUtils;
import lodsve.search.engine.SearchEngine;
import lodsve.search.engine.SolrSearchEngine;
import lodsve.search.properties.SearchProperties;
import lodsve.search.properties.SolrConfig;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * solr配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016/1/20 12:33
 */
@Configurable
@EnableConfigurationProperties(SearchProperties.class)
@ComponentScan("lodsve.search")
public class SolrConfiguration {
    @Autowired
    private SearchProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public HttpSolrClient solrClient() {
        String core = properties.getSolr().getCore();
        String server = properties.getSolr().getServer();

        if (StringUtils.isNotBlank(core)) {
            server += (StringUtils.endsWith(server, "/") ? "" : "/" + core);
        }

        HttpSolrClient.Builder builder = new HttpSolrClient.Builder();
        builder.withBaseSolrUrl(server);

        return builder.build();
    }

    @Bean
    public SearchEngine searchEngine(HttpSolrClient solrClient) {
        SolrConfig solr = properties.getSolr();

        return new SolrSearchEngine(solrClient, solr.getPrefix(), solr.getSuffix());
    }
}

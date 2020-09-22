/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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

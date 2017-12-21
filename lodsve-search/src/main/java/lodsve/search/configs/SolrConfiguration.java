package lodsve.search.configs;

import lodsve.core.autoconfigure.annotations.EnableConfigurationProperties;
import lodsve.core.condition.ConditionalOnMissingBean;
import lodsve.core.utils.StringUtils;
import lodsve.search.engine.SearchEngine;
import lodsve.search.engine.SolrSearchEngine;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * solr配置.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2016/1/20 12:33
 */
@Configurable
@ComponentScan("lodsve.search")
@EnableConfigurationProperties(SearchProperties.class)
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
        SearchProperties.Solr solr = properties.getSolr();

        return new SolrSearchEngine(solrClient, solr.getPrefix(), solr.getSuffix());
    }
}

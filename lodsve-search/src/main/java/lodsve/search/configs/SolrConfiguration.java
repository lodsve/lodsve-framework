package lodsve.search.configs;

import lodsve.core.autoconfigure.annotations.EnableConfigurationProperties;
import lodsve.search.engine.SearchEngine;
import lodsve.search.engine.SolrSearchEngine;
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
    public SearchEngine searchEngine() {
        return new SolrSearchEngine(properties.getServer(), properties.getCore(), properties.getPrefix(), properties.getSuffix());
    }
}

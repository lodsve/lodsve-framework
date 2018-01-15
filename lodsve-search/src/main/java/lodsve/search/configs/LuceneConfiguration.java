package lodsve.search.configs;

import lodsve.core.properties.autoconfigure.annotations.EnableConfigurationProperties;
import lodsve.search.engine.LuceneSearchEngine;
import lodsve.search.engine.SearchEngine;
import org.apache.lucene.analysis.Analyzer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * lucene配置.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2016/1/20 12:33
 */
@Configurable
@EnableConfigurationProperties(SearchProperties.class)
@ComponentScan("lodsve.search")
public class LuceneConfiguration {
    @Autowired
    private SearchProperties properties;

    @Bean
    public SearchEngine searchEngine() {
        SearchProperties.Lucene lucene = properties.getLucene();

        Class<?> analyzerClass = lucene.getAnalyzer();
        Analyzer analyzer = (Analyzer) BeanUtils.instantiate(analyzerClass);

        return new LuceneSearchEngine(lucene.getIndex(), analyzer, lucene.getPrefix(), lucene.getSuffix());
    }
}

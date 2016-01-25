package message.search.configs;

import message.search.engine.LuceneSearchEngine;
import message.search.engine.SearchEngine;
import net.paoding.analysis.analyzer.PaodingAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * lucene配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016/1/20 12:33
 */
@Configurable
@ComponentScan("message.search")
public class LuceneConfiguration {
    @Autowired
    private SearchProperties properties;

    @Bean
    public SearchEngine searchEngine() {
        LuceneSearchEngine searchEngine = new LuceneSearchEngine();
        searchEngine.setAnalyzer(new PaodingAnalyzer());
        searchEngine.setHtmlPrefix(properties.getPrefix());
        searchEngine.setHtmlSuffix(properties.getSuffix());
        searchEngine.setIndexPath(properties.getIndex());

        return searchEngine;
    }
}

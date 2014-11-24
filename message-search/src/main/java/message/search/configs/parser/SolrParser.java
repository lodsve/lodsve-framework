package message.search.configs.parser;

import message.config.SystemConfig;
import message.search.configs.SearchType;
import message.utils.PropertyPlaceholderHelper;
import org.w3c.dom.Element;

import java.util.Collections;
import java.util.Map;

/**
 * solr.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-9-14 下午3:42
 */
@SearchType("solr")
public class SolrParser extends AbstractSearchEngineParser {
    @Override
    protected Map<String, String> getConfigs(Element element) {
        String server_ = element.getAttribute("server");
        String server = PropertyPlaceholderHelper.processProperties(server_, false, SystemConfig.getAllConfigs());

        return Collections.singletonMap("server", server);
    }

    @Override
    protected String getTemplatePath() {
        return "META-INF/template/solr.xml";
    }
}

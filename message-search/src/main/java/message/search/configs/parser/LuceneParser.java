package message.search.configs.parser;

import message.config.SystemConfig;
import message.search.configs.SearchType;
import message.utils.PropertyPlaceholderHelper;
import org.w3c.dom.Element;

import java.util.Collections;
import java.util.Map;

/**
 * lucene.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-9-14 下午3:42
 */
@SearchType("lucene")
public class LuceneParser extends AbstractSearchEngineParser {
    @Override
    protected Map<String, String> getConfigs(Element element) {
        String index_ = element.getAttribute("index");
        String index = PropertyPlaceholderHelper.processProperties(index_, false, SystemConfig.getAllConfigs());

        return Collections.singletonMap("index", index);
    }

    @Override
    protected String getTemplatePath() {
        return "META-INF/template/lucene.xml";
    }
}

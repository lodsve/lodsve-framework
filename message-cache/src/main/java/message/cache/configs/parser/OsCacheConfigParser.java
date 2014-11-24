package message.cache.configs.parser;

import message.cache.configs.CacheType;
import message.config.SystemConfig;
import message.utils.PropertyPlaceholderHelper;
import org.w3c.dom.Element;

import java.util.Collections;
import java.util.Map;

/**
 * oscache配置解析.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-8-29 下午8:33
 */
@CacheType("oscache")
public class OsCacheConfigParser extends AbstractCacheConfigParser {
    @Override
    protected Map<String, String> getConfigs(Element element) {
        String configuration = element.getAttribute("configuration");
        String path = PropertyPlaceholderHelper.processProperties(configuration, false, SystemConfig.getAllConfigs());

        return Collections.singletonMap("configuration", path);
    }

    @Override
    protected String getCacheTemplate() {
        return "META-INF/template/oscache.xml";
    }
}

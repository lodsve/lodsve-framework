package message.cache.configs.parser;

import message.cache.configs.CacheType;
import message.config.SystemConfig;
import message.utils.PropertyPlaceholderHelper;
import org.w3c.dom.Element;

import java.util.Collections;
import java.util.Map;

/**
 * memcache配置解析.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-8-29 下午8:34
 */
@CacheType("memcache")
public class MemCacheConfigParser extends AbstractCacheConfigParser {
    @Override
    protected Map<String, String> getConfigs(Element element) {
        String servers = element.getAttribute("servers");
        String servers_ = PropertyPlaceholderHelper.processProperties(servers, false, SystemConfig.getAllConfigs());

        return Collections.singletonMap("servers", servers_);
    }

    @Override
    protected String getCacheTemplate() {
        return "META-INF/template/memcahce.xml";
    }
}

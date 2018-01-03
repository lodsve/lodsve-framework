package lodsve.core.io.support;

import lodsve.core.io.ZookeeperResource;
import lodsve.core.utils.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Lodsve ResourceLoader.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2018-1-2-0002 20:51
 */
public class LodsveResourceLoader extends DefaultResourceLoader {
    protected static final String URL_PREFIX = "zookeeper:";

    @Override
    public Resource getResource(String location) {
        Assert.notNull(location, "Location must not be null");
        if (StringUtils.startsWith(location, URL_PREFIX)) {
            return new ZookeeperResource(StringUtils.substringAfter(location, URL_PREFIX));
        }

        return super.getResource(location);
    }
}

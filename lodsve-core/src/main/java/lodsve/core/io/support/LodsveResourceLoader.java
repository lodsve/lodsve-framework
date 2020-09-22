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
package lodsve.core.io.support;

import lodsve.core.io.ZookeeperResource;
import lodsve.core.utils.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Lodsve ResourceLoader.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-1-2-0002 20:51
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

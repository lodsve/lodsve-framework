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
package lodsve.core;

import lodsve.core.configuration.BannerConfig;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

/**
 * 打印classpath下banner.txt.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-1-18-0018 10:06
 */
public class TextBanner implements Banner {
    private final Resource resource;

    TextBanner(Resource resource) {
        Assert.notNull(resource, "Text file must not be null");
        Assert.isTrue(resource.exists(), "Text file must exist");

        this.resource = resource;
    }

    @Override
    public void print(BannerConfig config, PrintStream out) {
        try {
            List<String> lines = IOUtils.readLines(resource.getInputStream(), config.getCharset());
            for (String line : lines) {
                out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

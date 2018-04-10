/*
 * Copyright (C) 2018  Sun.Hao
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.core;

import lodsve.core.configuration.ApplicationProperties;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;

/**
 * 打印classpath下banner.txt.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2018-1-18-0018 10:06
 */
public class TextBanner implements Banner {
    private Resource resource;

    TextBanner(Resource resource) {
        Assert.notNull(resource, "Text file must not be null");
        Assert.isTrue(resource.exists(), "Text file must exist");

        this.resource = resource;
    }

    @Override
    public void print(ApplicationProperties.BannerConfig config, PrintStream out) {
        try {
            String banner = StreamUtils.copyToString(this.resource.getInputStream(), Charset.forName(config.getCharset()));

            out.println(banner);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

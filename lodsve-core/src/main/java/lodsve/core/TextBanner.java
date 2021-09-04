/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lodsve.core;

import lodsve.core.configuration.properties.BannerConfig;
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

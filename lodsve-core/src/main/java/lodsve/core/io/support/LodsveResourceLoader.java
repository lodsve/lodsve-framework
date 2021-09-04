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

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
package lodsve.core.utils;

import lodsve.core.io.ZookeeperResource;
import lodsve.core.io.support.LodsveResourceLoader;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * resource utils.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018/1/6 上午1:34
 */
public final class ResourceUtils {
    private static final ResourceLoader RESOURCE_LOADER = new LodsveResourceLoader();

    private ResourceUtils() {
    }

    public static Resource getResource(String path) {
        return RESOURCE_LOADER.getResource(path);
    }

    public static String getPath(Resource resource) {
        Assert.notNull(resource);

        try {
            String pathToUse;
            if (resource instanceof ClassPathResource) {
                pathToUse = ((ClassPathResource) resource).getPath();
            } else if (resource instanceof FileSystemResource) {
                pathToUse = resource.getFile().getAbsolutePath();
            } else if (resource instanceof ZookeeperResource) {
                pathToUse = "zookeeper:" + ((ZookeeperResource) resource).getPath();
            } else {
                pathToUse = resource.getURL().getPath();
            }

            return pathToUse;
        } catch (IOException e) {
            return "";
        }
    }

    public static String getResourceProtocol(Resource resource) {
        Assert.notNull(resource);

        String protocol;
        if (resource instanceof ClassPathResource) {
            protocol = "classpath:";
        } else if (resource instanceof FileSystemResource) {
            protocol = "file:";
        } else if (resource instanceof ZookeeperResource) {
            protocol = "zookeeper:";
        } else {
            protocol = "";
        }

        return protocol;
    }

    public static String getContent(Resource resource, String fileEncoding) {
        Assert.notNull(resource);
        Assert.isTrue(resource.exists());

        try {
            return IOUtils.toString(resource.getInputStream(), StringUtils.isBlank(fileEncoding) ? "UTF-8" : fileEncoding);
        } catch (IOException e) {
            return "";
        }
    }

    public static String getContent(Resource resource) {
        return getContent(resource, "");
    }
}

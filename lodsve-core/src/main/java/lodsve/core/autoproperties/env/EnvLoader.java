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
package lodsve.core.autoproperties.env;

import lodsve.core.autoproperties.ParamsHome;
import lodsve.core.io.support.LodsvePathMatchingResourcePatternResolver;
import lodsve.core.io.support.LodsveResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * 配置文件解析.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 14-8-17 下午9:44
 */
public class EnvLoader {
    private static final Properties env = new Properties();

    private EnvLoader() {
    }

    public static Properties getFileEnvs(Resource... resources) {
        Properties prop = new Properties();

        loadProperties(prop, Arrays.asList(resources));

        return prop;
    }

    public static Properties getEnvs() {
        return env;
    }

    private static void loadProperties(Properties prop, List<Resource> resources) {
        for (Resource resource : resources) {
            try {
                PropertiesLoaderUtils.fillProperties(prop, new EncodedResource(resource, "UTF-8"));
            } catch (IOException e) {
                throw new RuntimeException("load配置文件发生异常");
            }
        }
    }

    public static Resource getFileEnv(String fileName) {
        ResourceLoader loader = new LodsveResourceLoader();

        return loader.getResource(ParamsHome.getInstance().getParamsRoot() + File.separator + "files" + File.separator + fileName);
    }

    public static Resource getFrameworkEnv(String fileName) {
        ResourceLoader loader = new LodsveResourceLoader();

        return loader.getResource(ParamsHome.getInstance().getParamsRoot() + File.separator + "framework" + File.separator + fileName);
    }

    public static void init() {
        ResourcePatternResolver resolver = new LodsvePathMatchingResourcePatternResolver();
        List<Resource> resources = new ArrayList<>();

        try {
            resources.addAll(Arrays.asList(resolver.getResources(ParamsHome.getInstance().getParamsRoot() + "/*.properties")));
            resources.addAll(Arrays.asList(resolver.getResources(ParamsHome.getInstance().getParamsRoot() + "/framework/*.properties")));
        } catch (IOException e) {
            return;
        }

        loadProperties(env, resources);

        // 获取覆盖的值
        ParamsHome.getInstance().coveredWithExtResource(env);

        env.put("params.root", ParamsHome.getInstance().getParamsRoot());
    }
}

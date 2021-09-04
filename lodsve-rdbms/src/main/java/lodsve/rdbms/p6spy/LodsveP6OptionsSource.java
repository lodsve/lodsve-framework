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
package lodsve.rdbms.p6spy;

import com.p6spy.engine.common.P6Util;
import com.p6spy.engine.spy.P6ModuleManager;
import com.p6spy.engine.spy.option.P6OptionsSource;
import lodsve.core.autoproperties.relaxedbind.RelaxedBindFactory;
import lodsve.core.utils.PropertyPlaceholderHelper;
import lodsve.rdbms.properties.P6SpyProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Lodsve P6 OptionsSource.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017/12/25 下午11:24
 */
public class LodsveP6OptionsSource implements P6OptionsSource {
    private static Resource config;
    private final Map<String, String> options;

    public static void init() {
        P6SpyProperties properties = new RelaxedBindFactory.Builder<>(P6SpyProperties.class).build();

        config = properties.getConfig();
        if (config == null || !config.exists()) {
            throw new IllegalArgumentException(String.format("p6spy的配置文件'{%s}'不能为空也不能不存在!", config));
        }
    }

    /**
     * Creates a new instance and loads the properties file if found.
     *
     * @throws IOException IOException
     */
    public LodsveP6OptionsSource() throws IOException {
        Properties properties = new Properties();
        PropertiesLoaderUtils.fillProperties(properties, config);
        options = P6Util.getPropertiesMap(properties);
    }

    @Override
    public Map<String, String> getOptions() {
        Set<String> keys = options.keySet();
        keys.forEach(key -> {
            String value = options.get(key);
            value = PropertyPlaceholderHelper.replacePlaceholder(value, true);

            options.put(key, value);
        });

        return options;
    }

    @Override
    public void postInit(P6ModuleManager p6moduleManager) {
    }

    @Override
    public void preDestroy(P6ModuleManager p6moduleManager) {
    }
}

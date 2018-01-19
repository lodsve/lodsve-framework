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

package lodsve.mybatis.p6spy;

import com.p6spy.engine.common.P6Util;
import com.p6spy.engine.spy.P6ModuleManager;
import com.p6spy.engine.spy.option.P6OptionsSource;
import lodsve.core.properties.Env;
import lodsve.core.properties.autoconfigure.PropertiesConfigurationFactory;
import lodsve.core.utils.PropertyPlaceholderHelper;
import lodsve.mybatis.properties.P6spyProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Lodsve P6 OptionsSource.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/12/25 下午11:24
 */
public class LodsveP6OptionsSource implements P6OptionsSource {
    private static Resource config;
    private final Map<String, String> options;

    public static void init() {
        P6spyProperties properties = new PropertiesConfigurationFactory.Builder<>(P6spyProperties.class).build();

        config = properties.getConfig();
        if (config == null || !config.exists()) {
            throw new IllegalArgumentException(String.format("p6spy的配置文件'{%s}'不能为空也不能不存在!", config));
        }
    }

    /**
     * Creates a new instance and loads the properties file if found.
     *
     * @throws java.io.IOException
     */
    public LodsveP6OptionsSource() throws IOException {
        Properties properties = new Properties();
        PropertiesLoaderUtils.fillProperties(properties, config);
        options = P6Util.getPropertiesMap(properties);
    }

    @Override
    public Map<String, String> getOptions() {
        Set<String> keys = options.keySet();
        Map<String, String> configs = Env.getEnvs();
        configs.putAll(Env.getSystemEnvs());
        for (String key : keys) {
            String value = options.get(key);
            value = PropertyPlaceholderHelper.replacePlaceholder(value, true, configs);

            options.put(key, value);
        }

        return options;
    }

    @Override
    public void postInit(P6ModuleManager p6moduleManager) {
    }

    @Override
    public void preDestroy(P6ModuleManager p6moduleManager) {
    }
}

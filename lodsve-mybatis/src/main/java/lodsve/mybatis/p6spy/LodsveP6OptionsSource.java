package lodsve.mybatis.p6spy;

import com.p6spy.engine.common.P6Util;
import com.p6spy.engine.spy.P6ModuleManager;
import com.p6spy.engine.spy.option.P6OptionsSource;
import lodsve.core.properties.Env;
import lodsve.core.properties.autoconfigure.AutoConfigurationBuilder;
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
        P6spyProperties properties = new AutoConfigurationBuilder.Builder<>(P6spyProperties.class).build();

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

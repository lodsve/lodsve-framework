package message.config.properties;

import static message.config.core.InitConfigPath.PARAMS_ROOT;

import message.config.exception.ConfigException;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * 配置文件解析.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-8-17 下午9:44
 */
public class ConfigurationLoader {
    public static Properties getConfigFileProperties(Resource... resources) throws IOException {
        Properties prop = new Properties();

        loadProperties(prop, resources);

        prop.put("params.root", PARAMS_ROOT);
        return prop;
    }

    public static Properties getConfigProperties() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("file:" + PARAMS_ROOT + "/*.properties");

        Properties prop = new Properties();

        loadProperties(prop, resources);

        prop.put("params.root", PARAMS_ROOT);
        return prop;
    }

    private static void loadProperties(Properties prop, Resource[] resources) {
        for (Resource resource : resources) {
            try {
                PropertiesLoaderUtils.fillProperties(prop, resource);
            } catch (IOException e) {
                throw new ConfigException(10008, e, "load配置文件发生异常");
            }
        }
    }

    public static Resource getConfigFile(String fileName) {
        ResourceLoader loader = new DefaultResourceLoader();

        return loader.getResource("file:" + PARAMS_ROOT + File.separator + "files" + File.separator + fileName);
    }
}

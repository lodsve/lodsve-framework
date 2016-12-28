package lodsve.core.config.properties;

import lodsve.core.config.core.ParamsHomeListener;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
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
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-8-17 下午9:44
 */
public class ConfigurationLoader {
    private static Properties prop = new Properties();

    private ConfigurationLoader() {
    }

    public static Properties getConfigFileProperties(Resource... resources) throws IOException {
        Properties prop = new Properties();

        loadProperties(prop, Arrays.asList(resources));

        return prop;
    }

    public static Properties getConfigProperties() {
        return prop;
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

    public static Resource getFileConfig(String fileName) {
        ResourceLoader loader = new DefaultResourceLoader();

        return loader.getResource("file:" + ParamsHomeListener.getParamsRoot() + File.separator + "files" + File.separator + fileName);
    }

    public static Resource getFrameworkConfig(String fileName) {
        ResourceLoader loader = new DefaultResourceLoader();

        return loader.getResource("file:" + ParamsHomeListener.getParamsRoot() + File.separator + "framework" + File.separator + fileName);
    }

    public static void init() throws Exception {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<Resource> resources = new ArrayList<>();

        resources.addAll(Arrays.asList(resolver.getResources("file:" + ParamsHomeListener.getParamsRoot() + "/*.properties")));
        resources.addAll(Arrays.asList(resolver.getResources("file:" + ParamsHomeListener.getParamsRoot() + "/framework/*.properties")));

        loadProperties(prop, resources);
        prop.put("params.root", ParamsHomeListener.getParamsRoot());
    }
}

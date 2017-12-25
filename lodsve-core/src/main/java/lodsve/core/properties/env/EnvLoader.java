package lodsve.core.properties.env;

import lodsve.core.properties.init.ParamsHome;
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
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 14-8-17 下午9:44
 */
public class EnvLoader {
    private static Properties env = new Properties();

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
        ResourceLoader loader = new DefaultResourceLoader();

        return loader.getResource("file:" + ParamsHome.getInstance().getParamsRoot() + File.separator + "files" + File.separator + fileName);
    }

    public static Resource getFrameworkEnv(String fileName) {
        ResourceLoader loader = new DefaultResourceLoader();

        return loader.getResource("file:" + ParamsHome.getInstance().getParamsRoot() + File.separator + "framework" + File.separator + fileName);
    }

    public static void init() {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<Resource> resources = new ArrayList<>();

        try {
            resources.addAll(Arrays.asList(resolver.getResources("file:" + ParamsHome.getInstance().getParamsRoot() + "/*.properties")));
            resources.addAll(Arrays.asList(resolver.getResources("file:" + ParamsHome.getInstance().getParamsRoot() + "/framework/*.properties")));
        } catch (IOException e) {
            return;
        }

        loadProperties(env, resources);

        // 获取覆盖的值
        ParamsHome.getInstance().coveredWithExtResource(env);

        env.put("params.root", ParamsHome.getInstance().getParamsRoot());
    }
}

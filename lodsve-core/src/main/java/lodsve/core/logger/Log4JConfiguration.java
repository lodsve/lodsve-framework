package lodsve.core.logger;

import lodsve.core.properties.Env;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * log4j的配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/5/26 下午2:44
 */
public class Log4JConfiguration {

    public static void init() {
        Resource resource = new ClassPathResource("/META-INF/log4j.properties", Thread.currentThread().getContextClassLoader());

        if (!resource.exists()) {
            resource = Env.getFileEnv("log4j.properties");
        }

        if (resource == null || !resource.exists()) {
            throw new RuntimeException("配置文件'log4j.properties'找不到！");
        }

        Properties prop = null;
        try {
            prop = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            System.err.println("no log4j configuration file!");
        }

        PropertyConfigurator.configure(prop);
    }
}

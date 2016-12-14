package lodsve.core.logger;

import lodsve.core.config.SystemConfig;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * log4j的配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/5/26 下午2:44
 */
@Component
public class Log4JConfiguration implements InitializingBean {

    public void afterPropertiesSet() throws Exception {
        Resource resource = SystemConfig.getFileConfig("log4j.properties");

        if (resource == null || !resource.exists()) {
            resource = new ClassPathResource("/META-INF/log4j.properties", Thread.currentThread().getContextClassLoader());
        }

        if (!resource.exists()) {
            throw new RuntimeException("配置文件'log4j.properties'找不到！");
        }

        Properties prop = PropertiesLoaderUtils.loadProperties(resource);

        PropertyConfigurator.configure(prop);
    }
}

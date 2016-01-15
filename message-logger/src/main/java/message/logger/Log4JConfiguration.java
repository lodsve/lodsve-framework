package message.logger;

import message.config.SystemConfig;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * log4j配置文件的载入
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-3-14 下午11:27:16
 */
@Component
public class Log4JConfiguration implements InitializingBean {

    public void afterPropertiesSet() throws Exception {
        Resource resource = SystemConfig.getConfigFile("log4j.properties");

        if (resource == null || !resource.exists()) {
            resource = new ClassPathResource("/META-INF/log4j.properties", Thread.currentThread().getContextClassLoader());
        }
        if (resource == null || !resource.exists()) {
            throw new LoggerException(10005, "配置文件'log4j.properties'找不到！");
        }

        Properties prop = PropertiesLoaderUtils.loadProperties(resource);

        PropertyConfigurator.configure(prop);
    }

}

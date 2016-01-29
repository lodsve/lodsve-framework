package message.config.core;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import message.base.utils.StringUtils;
import message.config.loader.ini.IniLoader;
import message.config.loader.properties.ConfigurationLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * 读取web.xml中设置的context-param[paramsHome].
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2015-1-5 10:00
 * @see message.config.core.InitConfigPath
 */
public class ParamsHomeListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ParamsHomeListener.class);
    private static final String SYSTEM_PARAM_PATH = "params.home";
    private static final String ENV_PARAM_PATH = "PARAMS_HOME";
    private static final String ROOT_PARAM_KEY = "config.root";
    private static final String ROOT_PARAM_FILE_NAME = "root.properties";
    private static final String PREFIX_SYSTEM = "system:";
    private static final String PREFIX_CLASSPATH = "classpath:";

    private static final String PARAMS_HOME = "paramsHome";

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String paramsHome = servletContextEvent.getServletContext().getInitParameter(PARAMS_HOME);
        logger.debug("get init parameter '{}' from web.xml is '{}'", PARAMS_HOME, paramsHome);

        init(paramsHome);

        try {
            ConfigurationLoader.init();
            IniLoader.init();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static void init(String defaultParamsHome) {
        //1.默认是在web.xml中配置
        String paramsPath = defaultParamsHome;

        //2.启动参数获取
        if (StringUtils.isEmpty(paramsPath)) {
            paramsPath = System.getProperty(SYSTEM_PARAM_PATH);
        }

        //3.环境变量获取
        if (StringUtils.isEmpty(paramsPath)) {
            paramsPath = System.getenv(ENV_PARAM_PATH);
        }

        if (StringUtils.isEmpty(paramsPath)) {
            throw new RuntimeException("读取配置文件错误！需要设置web.xml参数或者启动参数[params.home]或者环境变量[PARAMS_HOME]，并且web.xml中配置 > 启动参数 > 环境变量");
        }

        //判断路径是否含有classpath或者file
        if (StringUtils.indexOf(paramsPath, PREFIX_CLASSPATH) == 0) {
            paramsPath = StringUtils.removeStart(paramsPath, PREFIX_CLASSPATH);
            Resource resource = new ClassPathResource(paramsPath, InitConfigPath.class.getClassLoader());
            if (resource == null) {
                throw new RuntimeException("参数配置文件[" + (paramsPath) + "]不存在");
            }

            try {
                paramsPath = resource.getURL().getPath();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException("解析配置文件路径异常!");
            }
        } else if (StringUtils.indexOf(paramsPath, PREFIX_SYSTEM) == 0) {
            paramsPath = StringUtils.removeStart(paramsPath, PREFIX_SYSTEM);
        }

        Resource paramsResource = new FileSystemResource(paramsPath + File.separator + ROOT_PARAM_FILE_NAME);
        if (!paramsResource.exists()) {
            throw new RuntimeException("参数配置文件[" + (paramsPath + File.separator + ROOT_PARAM_FILE_NAME) + "]不存在");
        }

        Properties properties;
        try {
            properties = PropertiesLoaderUtils.loadProperties(paramsResource);
        } catch (IOException e) {
            throw new RuntimeException("加载配置文件[" + paramsResource + "]发生IO异常");
        }

        String root = properties.getProperty(ROOT_PARAM_KEY);
        if (StringUtils.isEmpty(root)) {
            throw new RuntimeException("配置文件中没有rootKey:[" + ROOT_PARAM_KEY + "]");
        }

        String paramsRoot = paramsPath + File.separator + root;
        InitConfigPath.setParamsRoot(paramsRoot);
        logger.debug("获取到的配置文件路径为:'{}'", paramsRoot);
    }
}

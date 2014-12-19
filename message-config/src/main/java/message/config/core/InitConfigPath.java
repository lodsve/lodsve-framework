package message.config.core;

import message.config.exception.ConfigException;
import message.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * 初始化配置文件的路径.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 14-9-2 下午11:53
 */
public abstract class InitConfigPath {
    private static final Logger logger = LoggerFactory.getLogger(InitConfigPath.class);

    private static final String SYSTEM_PARAM_PATH = "params.home";
    private static final String ENV_PARAM_PATH = "PARAMS_HOME";
    private static final String ROOT_PARAM_KEY = "config.root";
    public static String PARAMS_ROOT;

    static {
        init();
    }

    private InitConfigPath() {
    }

    private static void init() {
        String paramsPath;
        //1.启动参数获取
        paramsPath = System.getProperty(SYSTEM_PARAM_PATH);
        if (StringUtils.isEmpty(paramsPath)) {
            //2.环境变量获取
            paramsPath = System.getenv(ENV_PARAM_PATH);
        }

        if (StringUtils.isEmpty(paramsPath)) {
            throw new ConfigException(10008, "读取配置文件错误！需要设置启动参数[params.home]或者环境变量[PARAMS_HOME]，并且启动参数优先级大于环境变量");
        }

        Resource paramsResource = new FileSystemResource(paramsPath + File.separator + "root.properties");
        if (!paramsResource.exists()) {
            throw new ConfigException(10008, "参数配置文件[" + (paramsPath + File.separator + "root.properties") + "]不存在");
        }

        Properties properties;
        try {
            properties = PropertiesLoaderUtils.loadProperties(paramsResource);
        } catch (IOException e) {
            throw new ConfigException(10008, "加载配置文件[" + paramsResource + "]发生IO异常");
        }

        String root = properties.getProperty(ROOT_PARAM_KEY);
        if (StringUtils.isEmpty(root)) {
            throw new ConfigException(10008, "配置文件中没有rootKey:[" + ROOT_PARAM_KEY + "]");
        }

        PARAMS_ROOT = paramsPath + File.separator + root;
        logger.debug("获取到的配置文件路径为:'{}'", PARAMS_ROOT);
    }
}

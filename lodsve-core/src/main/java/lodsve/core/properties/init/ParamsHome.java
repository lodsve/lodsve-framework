package lodsve.core.properties.init;

import lodsve.core.utils.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * 初始化ParamsHome路径.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2017/11/30 下午6:48
 */
public class ParamsHome {
    private static ParamsHome instance = new ParamsHome();

    private static final String ROOT_PARAM_KEY = "config.root";
    /**
     * 容器启动参数
     */
    public static final String JVM_PARAM_PATH = "params.home";
    /**
     * 环境变量
     */
    private static final String ENV_PARAM_PATH = "PARAMS_HOME";
    /**
     * web.xml中配置
     */
    static final String PARAMS_HOME_NAME = "paramsHome";
    /**
     * 此文件只配置在JVM启动参数中，key-value将覆盖原来的值，而且这里的文件需要时绝对路径[仅仅在开发环境起作用!!!]
     */
    private static final String EXT_PARAMS_FILE_NAME = "ext.params.file";

    private static final String ROOT_PARAM_FILE_NAME = "root.properties";
    private static final String PREFIX_FILE = "file:";
    private static final String PREFIX_CLASSPATH = "classpath:";
    private static final Properties EXT_PARAMS_PROPERTIES = new Properties();

    /**
     * 配置文件路径
     */
    private static String PARAMS_ROOT = null;

    /**
     * 构造器私有，不可在外部进行初始化实例
     */
    private ParamsHome() {

    }

    public String getParamsRoot() {
        return PARAMS_ROOT;
    }

    public void cleanParamsRoot() {
        PARAMS_ROOT = null;
    }

    public void init(String webXmlParamsPath) {
        //1.默认是环境变量
        String paramsPath = System.getenv(ENV_PARAM_PATH);

        //2.JVM启动参数获取
        if (StringUtils.isEmpty(paramsPath)) {
            paramsPath = System.getProperty(JVM_PARAM_PATH);
        }

        //3.在web.xml中配置的
        if (StringUtils.isEmpty(paramsPath)) {
            paramsPath = webXmlParamsPath;
        }

        if (StringUtils.isEmpty(paramsPath)) {
            throw new RuntimeException("读取配置文件错误！需要设置环境变量[PARAMS_HOME]或者容器启动参数[params.home]或者web.xml参数，并且环境变量 > 容器启动参数 > web.xml中配置!");
        }

        System.out.println(String.format("解析得到的paramsPath为【%s】", paramsPath));

        //判断路径是否含有classpath或者file
        if (StringUtils.indexOf(paramsPath, PREFIX_CLASSPATH) == 0) {
            paramsPath = StringUtils.removeStart(paramsPath, PREFIX_CLASSPATH);
            Resource resource = new ClassPathResource(paramsPath, ParamsHome.class.getClassLoader());

            try {
                paramsPath = resource.getURL().getPath();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                throw new RuntimeException("解析配置文件路径异常!");
            }
        } else if (StringUtils.indexOf(paramsPath, PREFIX_FILE) == 0) {
            paramsPath = StringUtils.removeStart(paramsPath, PREFIX_FILE);
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

        PARAMS_ROOT = org.springframework.util.StringUtils.cleanPath(paramsPath + File.separator + root);
        System.out.println(String.format("获取到的配置文件路径为:'%s'", PARAMS_ROOT));

        // 获取devMode的值
        if (isDevMode()) {
            initExtResources();
        }
    }

    private boolean isDevMode() {
        String filePath = PARAMS_ROOT + File.separator + "framework" + File.separator + "application.properties";
        Resource resource = new FileSystemResource(filePath);
        if (!resource.exists()) {
            return false;
        }

        Properties prop = new Properties();
        try {
            PropertiesLoaderUtils.fillProperties(prop, new EncodedResource(resource, "UTF-8"));
            return Boolean.valueOf(prop.getProperty("application.devMode"));
        } catch (IOException e) {
            return false;
        }
    }

    private static void initExtResources() {
        String filePath = System.getProperty(EXT_PARAMS_FILE_NAME);
        if (StringUtils.isBlank(filePath)) {
            return;
        }

        System.out.println(String.format("获取到%s路径为'{%s}'!", EXT_PARAMS_FILE_NAME, filePath));

        Resource resource = new FileSystemResource(filePath);
        if (!resource.exists()) {
            System.err.println(String.format("找不到外部文件'[%s]'!", filePath));
            return;
        }

        try {
            PropertiesLoaderUtils.fillProperties(EXT_PARAMS_PROPERTIES, new EncodedResource(resource, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(String.format("读取路径为'%s'的文件失败！失败原因是'%s'!", filePath, e.getMessage()));
        }
    }

    public void coveredWithExtResource(Properties systemProperties) {
        if (EXT_PARAMS_PROPERTIES.isEmpty()) {
            return;
        }

        for (Map.Entry<Object, Object> entry : EXT_PARAMS_PROPERTIES.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            if (systemProperties.containsKey(key)) {
                systemProperties.put(key, value);
            }
        }
    }

    public static ParamsHome getInstance() {
        return instance;
    }
}

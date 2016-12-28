package lodsve.core.config.core;

import lodsve.core.config.ini.IniLoader;
import lodsve.core.config.properties.ConfigurationLoader;
import lodsve.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * 读取web.xml中设置的context-param[paramsHome].
 * 初始化配置文件的路径.<br/>
 * 配置文件加载顺序：<br/>
 * 环境变量 &gt; 启动参数 &gt; web.xml中配置<br/>
 * <ol>
 * <li>
 * web.xml中配置
 * <ul>
 * <li>
 * 配置context-param
 * <pre>
 *  &lt;context-param&gt;
 *     &lt;param-name&gt;paramsHome&lt;/param-name&gt;
 *     &lt;param-value&gt;your params home&lt;/param-value&gt;
 *  &lt;/context-param&gt;
 * </pre>
 * </li>
 * <li>
 * 配置listener[应该配置在web.xml中的所有listener之前，优先加载]
 * <pre>
 *  &lt;listener&gt;
 *      &lt;listener-class&gt;message.config.core.ParamsHomeListener&lt;/listener-class&gt;
 *  &lt;/listener&gt;
 * </pre>
 * </li>
 * </ul>
 * </li>
 * <li>
 * 启动参数<br/>
 * {@code -params.home=your params home }
 * </li>
 * <li>
 * 环境变量<br/>
 * 系统环境变量设置{@code PARAMS_HOME=you params home }
 * </li>
 * </ol>
 * 如果在classpath下,可以加上前缀classpath:you params home<br/>
 * 如果在文件系统中,可加前缀system:或者不加也行
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2015-1-5 10:00
 */
public class ParamsHomeListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ParamsHomeListener.class);

    private static final String ROOT_PARAM_KEY = "config.root";
    private static final String SYSTEM_PARAM_PATH = "params.home";
    private static final String ENV_PARAM_PATH = "PARAMS_HOME";
    private static final String ROOT_PARAM_FILE_NAME = "root.properties";
    private static final String PREFIX_SYSTEM = "system:";
    private static final String PREFIX_CLASSPATH = "classpath:";
    private static final String PARAMS_HOME_NAME = "paramsHome";

    // 配置文件路径
    private static String paramsRoot;

    public static String getParamsRoot() {
        if (StringUtils.isBlank(paramsRoot)) {
            init(StringUtils.EMPTY);
        }

        return paramsRoot;
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        paramsRoot = null;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String paramsHome = servletContextEvent.getServletContext().getInitParameter(PARAMS_HOME_NAME);
        logger.debug("get init parameter '{}' from web.xml is '{}'", PARAMS_HOME_NAME, paramsHome);

        init(paramsHome);

        try {
            ConfigurationLoader.init();
            IniLoader.init();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static void init(String webXmlParamsHome) {
        //1.默认是环境变量
        String paramsPath = System.getenv(ENV_PARAM_PATH);

        //2.启动参数获取
        if (StringUtils.isEmpty(paramsPath)) {
            paramsPath = System.getProperty(SYSTEM_PARAM_PATH);
        }

        //3.在web.xml中配置的
        if (StringUtils.isEmpty(paramsPath)) {
            paramsPath = webXmlParamsHome;
        }

        if (StringUtils.isEmpty(paramsPath)) {
            throw new RuntimeException("读取配置文件错误！需要设置环境变量[PARAMS_HOME]或者启动参数[params.home]或者web.xml参数，并且环境变量 > 启动参数 > web.xml中配置!");
        }

        logger.debug(String.format("解析得到的paramsPath为【%s】", paramsPath));

        //判断路径是否含有classpath或者file
        if (StringUtils.indexOf(paramsPath, PREFIX_CLASSPATH) == 0) {
            paramsPath = StringUtils.removeStart(paramsPath, PREFIX_CLASSPATH);
            Resource resource = new ClassPathResource(paramsPath, ParamsHomeListener.class.getClassLoader());

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

        paramsRoot = paramsPath + File.separator + root;
        logger.debug("获取到的配置文件路径为:'{}'", paramsRoot);
    }
}

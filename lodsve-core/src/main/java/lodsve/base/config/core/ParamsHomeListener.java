package lodsve.base.config.core;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import lodsve.base.config.loader.ini.IniLoader;
import lodsve.base.config.loader.properties.ConfigurationLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 读取web.xml中设置的context-param[paramsHome].
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2015-1-5 10:00
 * @see InitConfigPath
 */
public class ParamsHomeListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ParamsHomeListener.class);

    private static final String PARAMS_HOME = "paramsHome";

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String paramsHome = servletContextEvent.getServletContext().getInitParameter(PARAMS_HOME);
        logger.debug("get init parameter '{}' from web.xml is '{}'", PARAMS_HOME, paramsHome);

        InitConfigPath.setWebXmlParamsHome(paramsHome);

        try {
            ConfigurationLoader.init();
            IniLoader.init();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}

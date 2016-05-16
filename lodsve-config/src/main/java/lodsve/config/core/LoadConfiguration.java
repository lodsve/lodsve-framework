package lodsve.config.core;

import lodsve.config.loader.properties.ConfigurationLoader;
import lodsve.config.loader.ini.IniLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/5/5 下午2:53
 */
@Component
public class LoadConfiguration implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(LoadConfiguration.class);
    private static final boolean IS_SERVLET = ClassUtils.isPresent("javax.servlet.Servlet", Thread.currentThread().getContextClassLoader());

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!IS_SERVLET) {
            try {
                ConfigurationLoader.init();
                IniLoader.init();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}

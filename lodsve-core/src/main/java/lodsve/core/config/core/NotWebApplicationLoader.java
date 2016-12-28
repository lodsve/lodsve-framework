package lodsve.core.config.core;

import lodsve.core.condition.ConditionalOnNotWebApplication;
import lodsve.core.config.ini.IniLoader;
import lodsve.core.config.properties.ConfigurationLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * 非web应用时加载配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/5/5 下午2:53
 */
@Component
@ConditionalOnNotWebApplication
public class NotWebApplicationLoader implements BeanFactoryPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(NotWebApplicationLoader.class);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            ConfigurationLoader.init();
            IniLoader.init();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
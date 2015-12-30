package message.config.core;

import message.config.annotations.Configuration;
import message.config.properties.ConfigurationLoader;
import message.config.properties.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * properties变对象的解析.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/12/29 下午10:36
 */
@Component
@DependsOn({"configurationLoader", "iniLoader", "messageSourceLoader"})
public class InitConfiguration implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(InitConfiguration.class);

    private message.config.properties.Configuration _configuration;
    private ApplicationContext context;

    @Override
    public void afterPropertiesSet() throws Exception {
        _configuration = new PropertiesConfiguration(ConfigurationLoader.getConfigProperties());

        List<Class<?>> beanDefinitions = findClassWhitAnnotation();
        for (Class<?> bean : beanDefinitions) {
            Configuration configuration = bean.getAnnotation(Configuration.class);

            initBean(bean, configuration);
        }
    }

    private List<Class<?>> findClassWhitAnnotation() {
        BeanDefinitionRegistry bdr = new SimpleBeanDefinitionRegistry();
        ClassPathBeanDefinitionScanner s = new ClassPathBeanDefinitionScanner(bdr);

        TypeFilter tf = new AnnotationTypeFilter(Configuration.class);
        s.setIncludeAnnotationConfig(false);
        s.addIncludeFilter(tf);
        s.scan("*");

        String[] beans = bdr.getBeanDefinitionNames();

        List<Class<?>> beanDefinitions = new ArrayList<>();
        for (String b : beans) {
            BeanDefinition bd = bdr.getBeanDefinition(b);
            try {
                Class<?> clazz = ClassUtils.forName(bd.getBeanClassName(), this.getClass().getClassLoader());
                if (clazz.isAnnotationPresent(Configuration.class)) {
                    beanDefinitions.add(clazz);
                }
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage(), e);
            }
        }

        return beanDefinitions;
    }

    private void initBean(Class<?> clazz, Configuration configuration) throws Exception {
        String beanName = clazz.getSimpleName();
        if (context.containsBean(beanName)) {
            return;
        }

        String prefix = configuration.prefix();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        Field[] fields = clazz.getDeclaredFields();

        Set<String> keys = _configuration.subset(prefix).getKeys();

        for (Field field : fields) {
            String fieldName = field.getName();
            Class<?> returnType = field.getType();

            if (keys.contains(fieldName)) {
                String key = prefix + "." + fieldName;
                if (Boolean.class.equals(returnType)) {
                    beanDefinitionBuilder.addPropertyValue(fieldName, _configuration.getBoolean(key));
                } else if (Long.class.equals(returnType)) {
                    beanDefinitionBuilder.addPropertyValue(fieldName, _configuration.getLong(key));
                } else if (Integer.class.equals(returnType)) {
                    beanDefinitionBuilder.addPropertyValue(fieldName, _configuration.getInt(key));
                } else if (String.class.equals(returnType)) {
                    beanDefinitionBuilder.addPropertyValue(fieldName, _configuration.getString(key));
                } else if (Double.class.equals(returnType)) {
                    beanDefinitionBuilder.addPropertyValue(fieldName, _configuration.getDouble(key));
                }
            }
        }

        registerInSpringContext(beanName, beanDefinitionBuilder);
    }

    private void registerInSpringContext(String beanName, BeanDefinitionBuilder beanDefinitionBuilder) {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) configurableApplicationContext.getBeanFactory();

        beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}

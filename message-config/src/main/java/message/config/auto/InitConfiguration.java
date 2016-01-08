package message.config.auto;

import message.config.auto.annotations.ConfigurationProperties;
import message.config.properties.Configuration;
import message.config.properties.ConfigurationLoader;
import message.config.properties.PropertiesConfiguration;
import message.utils.GenericsUtils;
import message.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private static final List<? extends Class<? extends Serializable>> SIMPLE_CLASS = Arrays.asList(Boolean.class, boolean.class, Long.class, long.class,
            Integer.class, int.class, String.class, Double.class, double.class);

    private Configuration _configuration;
    private ApplicationContext context;
    private BeanDefinitionRegistry beanDefinitionRegistry;

    @Override
    public void afterPropertiesSet() throws Exception {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
        beanDefinitionRegistry = (BeanDefinitionRegistry) configurableApplicationContext.getBeanFactory();

        _configuration = new PropertiesConfiguration(ConfigurationLoader.getConfigProperties());

        List<Class<?>> beanDefinitions = findClassWhitAnnotation();
        for (Class<?> bean : beanDefinitions) {
            ConfigurationProperties configuration = bean.getAnnotation(ConfigurationProperties.class);

            initBean(bean, configuration);
        }
    }

    private List<Class<?>> findClassWhitAnnotation() {
        BeanDefinitionRegistry bdr = new SimpleBeanDefinitionRegistry();
        ClassPathBeanDefinitionScanner s = new ClassPathBeanDefinitionScanner(bdr);

        TypeFilter tf = new AnnotationTypeFilter(ConfigurationProperties.class);
        s.setIncludeAnnotationConfig(false);
        s.addIncludeFilter(tf);
        s.scan("*");

        String[] beans = bdr.getBeanDefinitionNames();

        List<Class<?>> beanDefinitions = new ArrayList<>();
        for (String b : beans) {
            BeanDefinition bd = bdr.getBeanDefinition(b);
            try {
                Class<?> clazz = ClassUtils.forName(bd.getBeanClassName(), this.getClass().getClassLoader());
                if (clazz.isAnnotationPresent(ConfigurationProperties.class)) {
                    beanDefinitions.add(clazz);
                }
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage(), e);
            }
        }

        return beanDefinitions;
    }

    private void initBean(Class<?> clazz, ConfigurationProperties configuration) throws Exception {
        String beanName = clazz.getSimpleName();
        if (context.containsBean(beanName)) {
            return;
        }

        String prefix = configuration.prefix();
        BeanDefinitionBuilder beanDefinitionBuilder = generateBeanClass(prefix, clazz);

        registerInSpringContext(beanName, beanDefinitionBuilder);
    }

    private BeanDefinitionBuilder generateBeanClass(String prefix, Class<?> clazz) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            String fieldName = field.getName();
            Class<?> type = field.getType();

            String key = prefix + "." + fieldName;

            if (isSimpleType(type)) {
                Object value = generateForSimpleType(key, type);
                if (value != null) {
                    beanDefinitionBuilder.addPropertyValue(fieldName, value);
                }
            } else if (Map.class.equals(type)) {
                Map map = generateForMap(key, field);
                if (map != null && !map.isEmpty())
                    beanDefinitionBuilder.addPropertyValue(fieldName, map);
            } else {
                // 对象
                BeanDefinitionBuilder builder = generateBeanClass(key, type);
                beanDefinitionBuilder.addPropertyValue(fieldName, builder.getBeanDefinition());
            }
        }

        return beanDefinitionBuilder;
    }

    private Map generateForMap(String prefix, Field field) {
        if (!Map.class.equals(field.getType()) || !String.class.equals(GenericsUtils.getFieldGenericType0(field))) {
            return null;
        }

        Map map = new HashMap();
        Class<?> secondGenericClazz = GenericsUtils.getFieldGenericType(field, 1);
        Set<String> keys = _configuration.subset(prefix).getKeys();
        for (String key : keys) {
            String[] temp = StringUtils.split(key, ".");
            if (temp.length != 2) {
                continue;
            }

            String keyInMap = temp[0];
            Object object = generateObject(prefix + "." + keyInMap, secondGenericClazz);
            if (object != null)
                map.put(keyInMap, object);
        }

        return map;
    }

    private Object generateObject(String prefix, Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();

        Object object = BeanUtils.instantiate(clazz);

        try {
            for (Field field : fields) {
                field.setAccessible(true);

                String fieldName = field.getName();
                Class<?> type = field.getType();

                String key = prefix + "." + fieldName;

                if (isSimpleType(type)) {
                    Object value = generateForSimpleType(key, type);
                    if (value != null) {
                        field.set(object, value);
                    }
                } else if (Map.class.equals(type)) {
                    Map map = generateForMap(key, field);
                    if (map != null && !map.isEmpty())
                        field.set(object, map);
                } else {
                    // 对象
                    Object object_ = generateObject(key, type);
                    field.set(object, object_);
                }
            }
        } catch (Exception e) {
            return null;
        }

        return object;
    }

    private Object generateForSimpleType(String key, Class<?> type) {
        try {
            if (Boolean.class.equals(type) || boolean.class.equals(type)) {
                return _configuration.getBoolean(key);
            } else if (Long.class.equals(type) || long.class.equals(type)) {
                return _configuration.getLong(key);
            } else if (Integer.class.equals(type) || int.class.equals(type)) {
                return _configuration.getInt(key);
            } else if (String.class.equals(type)) {
                return _configuration.getString(key);
            } else if (Double.class.equals(type) || double.class.equals(type)) {
                return _configuration.getDouble(key);
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    private boolean isSimpleType(Class<?> type) {
        return SIMPLE_CLASS.contains(type);
    }

    private void registerInSpringContext(String beanName, BeanDefinitionBuilder beanDefinitionBuilder) {
        beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}

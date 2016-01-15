package message.config.auto;

import message.config.SystemConfig;
import message.config.auto.annotations.ConfigurationProperties;
import message.config.loader.properties.Configuration;
import message.config.loader.properties.ConfigurationLoader;
import message.config.loader.properties.PropertiesConfiguration;
import message.utils.GenericsUtils;
import message.utils.PropertyPlaceholderHelper;
import message.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/9 下午5:41
 */
@Component
@DependsOn({"configurationLoader", "iniLoader", "messageSourceLoader"})
public class ConfigurationPropertiesBindingPostProcessor implements BeanPostProcessor, InitializingBean {
    private static final List<? extends Class<? extends Serializable>> SIMPLE_CLASS = Arrays.asList(Boolean.class, boolean.class, Long.class, long.class,
            Integer.class, int.class, String.class, Double.class, double.class);
    private ResourceLoader resourceLoader = new DefaultResourceLoader();
    private Environment environment = new StandardEnvironment();

    private Configuration _configuration_;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        ConfigurationProperties annotation = AnnotationUtils.findAnnotation(bean.getClass(), ConfigurationProperties.class);

        Object target = bean;
        if (annotation != null) {
            try {
                target = initBean(bean.getClass(), annotation, findConfiguration(annotation));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return target;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        _configuration_ = new PropertiesConfiguration(ConfigurationLoader.getConfigProperties());
    }

    private Object initBean(Class<?> clazz, ConfigurationProperties configuration, Configuration _configuration) throws Exception {
        String prefix = configuration.prefix();
        return generateBeanClass(prefix, clazz, _configuration);
    }

    private Object generateBeanClass(String prefix, Class<?> clazz, Configuration _configuration) throws IllegalAccessException {
        Object object = BeanUtils.instantiate(clazz);

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            String fieldName = field.getName();
            Class<?> type = field.getType();

            String key = prefix + "." + fieldName;

            if (isSimpleType(type)) {
                Object value = generateForSimpleType(key, type, _configuration);
                if (value != null) {
                    field.set(object, value);
                }
            } else if (Map.class.equals(type)) {
                Map map = generateForMap(key, field, _configuration);
                if (map != null && !map.isEmpty()) {
                    field.set(object, map);
                }
            } else {
                // 对象
                Object object_ = generateBeanClass(key, type, _configuration);
                field.set(object, object_);
            }
        }

        return object;
    }

    private boolean isSimpleType(Class<?> type) {
        return SIMPLE_CLASS.contains(type);
    }

    private Object generateForSimpleType(String key, Class<?> type, Configuration _configuration) {
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

    private Map generateForMap(String prefix, Field field, Configuration _configuration) {
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
            Object object = generateObject(prefix + "." + keyInMap, secondGenericClazz, _configuration);
            if (object != null)
                map.put(keyInMap, object);
        }

        return map;
    }

    private Object generateObject(String prefix, Class<?> clazz, Configuration _configuration) {
        Field[] fields = clazz.getDeclaredFields();

        Object object = BeanUtils.instantiate(clazz);

        try {
            for (Field field : fields) {
                field.setAccessible(true);

                String fieldName = field.getName();
                Class<?> type = field.getType();

                String key = prefix + "." + fieldName;

                if (isSimpleType(type)) {
                    Object value = generateForSimpleType(key, type, _configuration);
                    if (value != null) {
                        field.set(object, value);
                    }
                } else if (Map.class.equals(type)) {
                    Map map = generateForMap(key, field, _configuration);
                    if (map != null && !map.isEmpty())
                        field.set(object, map);
                } else {
                    // 对象
                    Object object_ = generateObject(key, type, _configuration);
                    field.set(object, object_);
                }
            }
        } catch (Exception e) {
            return null;
        }

        return object;
    }

    private Configuration findConfiguration(ConfigurationProperties properties) throws FileNotFoundException {
        String[] locations = properties.locations();
        if (locations == null || locations.length == 0) {
            return _configuration_;
        }

        Properties prop = new Properties();
        for (String location : locations) {
            location = PropertyPlaceholderHelper.replacePlaceholder(location, location, SystemConfig.getAllConfigs());

            Resource resource = this.resourceLoader.getResource(this.environment.resolvePlaceholders(location));
            try {
                PropertiesLoaderUtils.fillProperties(prop, resource);
            } catch (IOException e) {
                throw new FileNotFoundException("File '" + location + "' does not exist");
            }
        }

        return new PropertiesConfiguration(prop);
    }
}

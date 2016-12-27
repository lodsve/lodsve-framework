package lodsve.core.autoconfigure;

import lodsve.core.config.SystemConfig;
import lodsve.core.autoconfigure.annotations.ConfigurationProperties;
import lodsve.core.autoconfigure.annotations.Required;
import lodsve.core.config.properties.Configuration;
import lodsve.core.config.properties.ConfigurationLoader;
import lodsve.core.config.properties.PropertiesConfiguration;
import lodsve.core.utils.GenericUtils;
import lodsve.core.utils.PropertyPlaceholderHelper;
import lodsve.core.utils.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 自动装配生成器.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-1-26 14:17
 */
public class AutoConfigurationCreator {
    private static final List<? extends Class<? extends Serializable>> SIMPLE_CLASS = Arrays.asList(Boolean.class, boolean.class, Long.class, long.class,
            Integer.class, int.class, String.class, Double.class, double.class);
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    private AutoConfigurationCreator() {
    }

    private <T> T generateConfigurationBean(Class<T> clazz, ConfigurationProperties annotation) throws Exception {
        Configuration configuration = loadProp(annotation.locations());
        return generateObject(annotation.prefix(), clazz, configuration);
    }

    private <T> T generateObject(String prefix, Class<T> clazz, Configuration configuration) throws Exception {
        T object = BeanUtils.instantiate(clazz);
        BeanWrapper beanWrapper = new BeanWrapperImpl(object);

        PropertyDescriptor[] descriptors = beanWrapper.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            if (descriptor.getWriteMethod() == null) {
                continue;
            }

            String name = descriptor.getName();
            Class<?> type = descriptor.getPropertyType();
            Method method = descriptor.getReadMethod();
            TypeDescriptor typeDescriptor = beanWrapper.getPropertyTypeDescriptor(name);
            Required required = typeDescriptor.getAnnotation(Required.class);

            String key = prefix + "." + name;
            Object value = getValue(type, key, method, configuration);

            if (value == null) {
                value = getValue(type, prefix + "." + getCamelName(name), method, configuration);
            }

            if (value != null) {
                beanWrapper.setPropertyValue(name, value);
            } else if (required != null) {
                throw new RuntimeException(String.format("property [%s]'s value can't be null!please check your config!", name));
            }
        }

        return object;
    }

    private Object getValue(Class<?> type, String key, Method method, Configuration configuration) throws Exception {
        Object value;

        if (isSimpleType(type)) {
            value = getValueForSimpleType(key, type, configuration);
        } else if (Map.class.equals(type)) {
            value = getValueForMap(key, method, configuration);
        } else {
            value = generateObject(key, type, configuration);
        }

        return value;
    }

    private Configuration loadProp(String... configLocations) throws Exception {
        if (ArrayUtils.isEmpty(configLocations)) {
            return new PropertiesConfiguration(ConfigurationLoader.getConfigProperties());
        }

        Properties prop = new Properties();
        for (String location : configLocations) {
            location = PropertyPlaceholderHelper.replacePlaceholder(location, true, SystemConfig.getAllConfigs());

            Resource resource = this.resourceLoader.getResource(location);
            PropertiesLoaderUtils.fillProperties(prop, new EncodedResource(resource, "UTF-8"));
        }

        return new PropertiesConfiguration(prop);
    }

    private boolean isSimpleType(Class<?> type) {
        return SIMPLE_CLASS.contains(type);
    }

    private Object getValueForSimpleType(String key, Class<?> type, Configuration configuration) {
        try {
            if (Boolean.class.equals(type) || boolean.class.equals(type)) {
                return configuration.getBoolean(key);
            } else if (Long.class.equals(type) || long.class.equals(type)) {
                return configuration.getLong(key);
            } else if (Integer.class.equals(type) || int.class.equals(type)) {
                return configuration.getInt(key);
            } else if (String.class.equals(type)) {
                return configuration.getString(key);
            } else if (Double.class.equals(type) || double.class.equals(type)) {
                return configuration.getDouble(key);
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    private Map<String, Object> getValueForMap(String prefix, Method method, Configuration configuration) throws Exception {
        if (!Map.class.equals(method.getReturnType()) || !String.class.equals(GenericUtils.getGenericParameter0(method))) {
            return null;
        }

        Map<String, Object> map = new HashMap<>();
        Class<?> secondGenericClazz = GenericUtils.getGenericParameter(method, 1);
        Set<String> keys = configuration.subset(prefix).getKeys();
        for (String key : keys) {
            String[] temp = StringUtils.split(key, ".");
            if (temp.length < 2) {
                continue;
            }

            String keyInMap = temp[0];
            Object object = generateObject(prefix + "." + keyInMap, secondGenericClazz, configuration);
            if (object != null)
                map.put(keyInMap, object);
        }

        return map;
    }

    private String getCamelName(String name) {
        Assert.hasText(name);

        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            for (int i = 0; i < name.length(); i++) {
                String tmp = name.substring(i, i + 1);
                //判断截获的字符是否是大写，大写字母的toUpperCase()还是大写的
                if (tmp.equals(tmp.toUpperCase())) {
                    //此字符是大写的
                    result.append("_").append(tmp.toLowerCase());
                } else {
                    result.append(tmp);
                }
            }
        }

        return result.toString();
    }

    public static class Builder<T> {
        private AutoConfigurationCreator creator = new AutoConfigurationCreator();
        private Class<T> clazz;
        private ConfigurationProperties annotation;

        public Builder<T> setClazz(Class<T> clazz) {
            this.clazz = clazz;
            return this;
        }

        public Builder<T> setAnnotation(ConfigurationProperties annotation) {
            this.annotation = annotation;
            return this;
        }

        public T build() throws Exception {
            return creator.generateConfigurationBean(clazz, annotation);
        }
    }
}

package lodsve.core.autoconfigure;

import lodsve.core.autoconfigure.annotations.ConfigurationProperties;
import lodsve.core.autoconfigure.annotations.Required;
import lodsve.core.properties.Env;
import lodsve.core.properties.core.ParamsHome;
import lodsve.core.properties.env.Configuration;
import lodsve.core.properties.env.PropertiesConfiguration;
import lodsve.core.utils.GenericUtils;
import lodsve.core.utils.NumberUtils;
import lodsve.core.utils.PropertyPlaceholderHelper;
import lodsve.core.utils.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 自动装配生成器.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2016-1-26 14:17
 */
public class AutoConfigurationBuilder {
    private static final Logger logger = LoggerFactory.getLogger(AutoConfigurationBuilder.class);

    private static final List<?> COMMON_TYPES = Arrays.asList(Boolean.class, boolean.class, Long.class, long.class,
            Integer.class, int.class, String.class, Double.class, double.class, Resource.class, Properties.class,
            Class.class);
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    private static final Map<Class<?>, Object> CLASS_OBJECT_MAPPING = new HashMap<>(16);
    private static Configuration configuration;
    private static final Map<String, String> configurations = new HashMap<>();

    private AutoConfigurationBuilder() {
    }

    @SuppressWarnings("unchecked")
    private <T> T generateConfigurationBean(Class<T> clazz, ConfigurationProperties annotation) {
        loadProp(annotation.locations());
        T object = (T) CLASS_OBJECT_MAPPING.get(clazz);
        if (object == null) {
            object = generateObject(annotation.prefix(), clazz);
            CLASS_OBJECT_MAPPING.put(clazz, object);
        }

        return object;
    }

    private <T> T generateObject(String prefix, Class<T> clazz) {
        T object = BeanUtils.instantiate(clazz);
        BeanWrapper beanWrapper = new BeanWrapperImpl(object);

        PropertyDescriptor[] descriptors = beanWrapper.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            if (descriptor.getWriteMethod() == null) {
                continue;
            }

            String name = descriptor.getName();
            Class<?> type = descriptor.getPropertyType();
            Method readMethod = descriptor.getReadMethod();
            TypeDescriptor typeDescriptor = beanWrapper.getPropertyTypeDescriptor(name);
            Required required = typeDescriptor.getAnnotation(Required.class);

            Object value = getValue(type, prefix, name, readMethod);

            if (value != null) {
                beanWrapper.setPropertyValue(name, value);
            } else if (required != null) {
                throw new RuntimeException(String.format("property [%s]'s value can't be null!please check your config!", name));
            }
        }

        return object;
    }

    private Object getValue(Class<?> type, String prefix, String name, Method readMethod) {
        String key = prefix;
        String camelName = name;
        if (StringUtils.isNotBlank(name)) {
            key += ("." + name);
            camelName = getCamelName(name);
        }
        Object value = getValueForType(key, type, readMethod);

        if (value == null) {
            value = getValueForType(prefix + "." + camelName, type, readMethod);
        }

        return value;
    }

    private Object getValueForType(String key, Class<?> type, Method readMethod) {
        Object value;

        if (isSimpleType(type)) {
            value = getValueForSimpleType(key, type);
        } else if (Map.class.equals(type)) {
            value = getValueForMap(key, readMethod);
        } else if (type.isArray()) {
            value = getValueForArray(key, type, readMethod);
        } else {
            value = generateObject(key, type);
        }

        return value;
    }

    private Object getValueForArray(String prefix, Class<?> type, Method readMethod) {
        Configuration subset = configuration.subset(prefix);
        Set<String> keys = subset.getKeys();
        Class clazz = type.getComponentType();

        List<Object> list = new ArrayList<>(keys.size());
        int size = getArraySize(keys);
        for (int i = 0; i < size; i++) {
            String key = prefix + "." + i;

            list.add(getValue(clazz, key, StringUtils.EMPTY, readMethod));
        }

        return list.toArray(new Object[list.size()]);
    }

    private int getArraySize(Set<String> keys) {
        Set<String> realKeyIndexs = new HashSet<>();
        for (String key : keys) {
            if (StringUtils.isBlank(key)) {
                continue;
            }
            // 取第一位
            String first = StringUtils.mid(key, 0, 1);
            realKeyIndexs.add(first);
        }

        return realKeyIndexs.size();
    }

    @SuppressWarnings("unchecked")
    private void loadProp(String... configLocations) {
        Properties prop = new Properties();
        prop.putAll(Env.getSystemEnvs());
        prop.putAll(Env.getEnvs());

        if (ArrayUtils.isEmpty(configLocations)) {
            ParamsHome.getInstance().coveredWithExtResource(prop);
            configuration = new PropertiesConfiguration(prop);
            configurations.putAll((Map) prop);
            return;
        }


        for (String location : configLocations) {
            location = PropertyPlaceholderHelper.replacePlaceholder(location, true, Env.getEnvs());

            Resource resource = this.resourceLoader.getResource(location);
            if (!resource.exists()) {
                continue;
            }

            try {
                PropertiesLoaderUtils.fillProperties(prop, new EncodedResource(resource, "UTF-8"));
            } catch (IOException e) {
                if (logger.isErrorEnabled()) {
                    logger.error(String.format("fill properties with file '%s' error!", resource.getFilename()));
                }
            }
        }

        // 获取覆盖的值
        ParamsHome.getInstance().coveredWithExtResource(prop);
        configuration = new PropertiesConfiguration(prop);
        configurations.putAll((Map) prop);
    }

    private boolean isSimpleType(Class<?> type) {
        return COMMON_TYPES.contains(type);
    }

    private Object getValueForSimpleType(String key, Class<?> type) {
        return evalValue(configuration.getString(key), type);
    }

    private Map<String, Object> getValueForMap(String prefix, Method readMethod) {
        if (!Map.class.equals(readMethod.getReturnType()) || !String.class.equals(GenericUtils.getGenericParameter0(readMethod))) {
            return null;
        }

        Map<String, Object> map = new HashMap<>(16);
        Class<?> secondGenericClazz = GenericUtils.getGenericParameter(readMethod, 1);
        Set<String> keys = configuration.subset(prefix).getKeys();
        for (String key : keys) {
            String[] temp = StringUtils.split(key, ".");
            if (temp.length < 2) {
                continue;
            }

            String keyInMap = temp[0];
            Object object = generateObject(prefix + "." + keyInMap, secondGenericClazz);
            if (object != null) {
                map.put(keyInMap, object);
            }
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
                if (!NumberUtils.isNumber(tmp) && tmp.equals(tmp.toUpperCase())) {
                    //此字符是大写的
                    result.append("-").append(tmp.toLowerCase());
                } else {
                    result.append(tmp);
                }
            }
        }

        return result.toString();
    }

    private Object evalValue(String value, Class<?> type) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        String text = PropertyPlaceholderHelper.replacePlaceholder(value, true, configurations);

        if (Boolean.class.equals(type) || boolean.class.equals(type)) {
            return Boolean.valueOf(text);
        } else if (Long.class.equals(type) || long.class.equals(type)) {
            return Long.valueOf(text);
        } else if (Integer.class.equals(type) || int.class.equals(type)) {
            return Integer.valueOf(text);
        } else if (String.class.equals(type)) {
            return text;
        } else if (Double.class.equals(type) || double.class.equals(type)) {
            return Double.valueOf(text);
        } else if (Resource.class.equals(type)) {
            return resourceLoader.getResource(text);
        } else if (Properties.class.equals(type)) {
            try {
                return PropertiesLoaderUtils.loadProperties(resourceLoader.getResource(text));
            } catch (IOException e) {
                if (logger.isErrorEnabled()) {
                    logger.error(String.format("file '{%s}' not found!", text));
                }
            }
        } else if (Class.class.equals(type)) {
            try {
                return ClassUtils.forName(text, AutoConfigurationBuilder.class.getClassLoader());
            } catch (ClassNotFoundException e) {
                if (logger.isErrorEnabled()) {
                    logger.error(String.format("class '{%s}' not found!", text));
                }
                return Void.class;
            }
        }

        return text;
    }

    public static class Builder<T> {
        private AutoConfigurationBuilder builder = new AutoConfigurationBuilder();
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

        public T build() {
            // check
            if (clazz == null) {
                throw new IllegalArgumentException("clazz is required!");
            }

            if (annotation == null) {
                throw new IllegalArgumentException("annotation is required!");
            }

            return builder.generateConfigurationBean(clazz, annotation);
        }
    }
}

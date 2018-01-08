package lodsve.core.properties.autoconfigure;

import lodsve.core.properties.Env;
import lodsve.core.properties.autoconfigure.annotations.ConfigurationProperties;
import lodsve.core.properties.autoconfigure.annotations.Required;
import lodsve.core.properties.env.Configuration;
import lodsve.core.properties.env.PropertiesConfiguration;
import lodsve.core.properties.init.ParamsHome;
import lodsve.core.utils.*;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 自动装配生成器.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2016-1-26 14:17
 */
public class PropertiesConfigurationFactory {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesConfigurationFactory.class);

    private static final List<?> COMMON_TYPES = Arrays.asList(Boolean.class, boolean.class, Long.class, long.class,
            Integer.class, int.class, String.class, Double.class, double.class, Resource.class, Properties.class,
            Class.class, File.class);

    private Properties propertySource;
    private Object target;
    private String targetName;
    private Configuration configuration;

    private void setPropertySource(Properties propertySource) {
        Assert.notNull(propertySource);
        this.propertySource = propertySource;
        configuration = new PropertiesConfiguration(propertySource);

    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    private static final Map<Class<?>, Object> CLASS_OBJECT_MAPPING = new HashMap<>(16);


    private PropertiesConfigurationFactory(Object target) {
        this.target = target;
    }

    @SuppressWarnings("unchecked")
    private void bindToTarget() {
        Object object = CLASS_OBJECT_MAPPING.get(target.getClass());
        if (object == null) {
            generateObject(targetName, target);
            CLASS_OBJECT_MAPPING.put(target.getClass(), target);
        }

        target = object;
    }

    private void generateObject(String prefix, Object target) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(target);

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
    }

    private Object getValue(Class<?> type, String prefix, String name, Method readMethod) {
        String key = prefix;
        String camelName = name;
        if (StringUtils.isNotBlank(name)) {
            key += ("." + name);
            camelName = getCamelName(name);
        }
        Object value = getValueForType(key, type, readMethod);

        if (value == null || (value instanceof Collection && ((Collection) value).isEmpty()) ||
                (value instanceof Map && ((Map) value).isEmpty()) ||
                (value.getClass().isArray() && ArrayUtils.isEmpty((Object[]) value))) {
            value = getValueForType(prefix + "." + camelName, type, readMethod);
        }

        return value;
    }

    private Object getValueForType(String key, Class<?> type, Method readMethod) {
        Object value;

        if (COMMON_TYPES.contains(type)) {
            value = getValueForSimpleType(key, type);
        } else if (Map.class.equals(type)) {
            value = getValueForMap(key, readMethod);
        } else if (type.isArray()) {
            value = getValueForArray(key, type.getComponentType(), readMethod);
        } else if (List.class.isAssignableFrom(type) || Set.class.isAssignableFrom(type)) {
            Class<?> clazz = GenericUtils.getGenericParameter0(readMethod);
            value = getValueForCollection(key, clazz, type, readMethod);
        } else {
            value = BeanUtils.instantiate(type);
            bindToSubTarget(value, key);
        }

        return value;
    }

    private void bindToSubTarget(Object target, String targetName) {
        PropertiesConfigurationFactory factory = new PropertiesConfigurationFactory(target);
        factory.setTargetName(targetName);
        factory.setPropertySource(propertySource);
        factory.bindToTarget();
    }

    @SuppressWarnings("unchecked")
    private Object getValueForSimpleType(String key, Class<?> type) {
        String value = configuration.getString(key);
        if (StringUtils.isBlank(value)) {
            return null;
        }

        String text = PropertyPlaceholderHelper.replacePlaceholder(value, true, (Map) propertySource);

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
            return ResourceUtils.getResource(text);
        } else if (File.class.equals(type)) {
            try {
                return ResourceUtils.getResource(text).getFile();
            } catch (IOException e) {
                return null;
            }
        } else if (Properties.class.equals(type)) {
            try {
                return PropertiesLoaderUtils.loadProperties(ResourceUtils.getResource(text));
            } catch (IOException e) {
                if (logger.isErrorEnabled()) {
                    logger.error(String.format("file '{%s}' not found!", text));
                }
                return null;
            }
        } else if (Class.class.equals(type)) {
            try {
                return ClassUtils.forName(text, PropertiesConfigurationFactory.class.getClassLoader());
            } catch (ClassNotFoundException e) {
                if (logger.isErrorEnabled()) {
                    logger.error(String.format("class '{%s}' not found!", text));
                }
                return null;
            }
        }

        return text;
    }

    private Map<String, Object> getValueForMap(String prefix, Method readMethod) {
        if (!Map.class.equals(readMethod.getReturnType()) || !String.class.equals(GenericUtils.getGenericParameter0(readMethod))) {
            return null;
        }

        Map<String, Object> map = new HashMap<>(16);
        Class<?> secondGenericClazz = GenericUtils.getGenericParameter(readMethod, 1);
        Assert.notNull(secondGenericClazz, "If use Map, must provider generic info!");
        Set<String> keys = configuration.subset(prefix).getKeys();
        for (String key : keys) {
            String[] temp = StringUtils.split(key, ".");
            if (temp.length < 2) {
                continue;
            }

            String keyInMap = temp[0];

            Object target = BeanUtils.instantiate(secondGenericClazz);
            bindToSubTarget(target, prefix + "." + keyInMap);

            if (target != null) {
                map.put(keyInMap, target);
            }
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    private Object getValueForCollection(String prefix, Class<?> param, Class<?> type, Method readMethod) {
        List<Object> result = getValuesForCollectionOrArray(prefix, param, readMethod);

        return List.class.isAssignableFrom(type) ? result : new HashSet(result);
    }

    private Object getValueForArray(String prefix, Class<?> type, Method readMethod) {
        List<Object> list = getValuesForCollectionOrArray(prefix, type, readMethod);

        return list.toArray(new Object[list.size()]);
    }

    private List<Object> getValuesForCollectionOrArray(String prefix, Class<?> type, Method readMethod) {
        Configuration subset = configuration.subset(prefix);
        Set<String> keys = subset.getKeys();

        List<Object> list = new ArrayList<>(keys.size());
        int size = getArraySize(keys);
        for (int i = 0; i < size; i++) {
            String key = prefix + ".[" + i + "]";

            list.add(getValue(type, key, StringUtils.EMPTY, readMethod));
        }

        return list;
    }

    private int getArraySize(Set<String> keys) {
        Set<String> realKeyIndexs = new HashSet<>();
        for (String key : keys) {
            if (StringUtils.isBlank(key)) {
                continue;
            }
            // 取第一位
            String first = StringUtils.mid(key, 1, 1);
            realKeyIndexs.add(first);
        }

        return realKeyIndexs.size();
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

    public static class Builder<T> {
        private Class<T> clazz;

        public Builder(Class<T> clazz) {
            this.clazz = clazz;
        }

        public T build() {
            // check
            if (clazz == null) {
                throw new IllegalArgumentException("clazz is required!");
            }

            ConfigurationProperties annotation = clazz.getAnnotation(ConfigurationProperties.class);

            if (annotation == null) {
                throw new IllegalArgumentException("annotation is required!");
            }

            T target = BeanUtils.instantiate(clazz);
            loadProp(annotation.locations());

            PropertiesConfigurationFactory factory = new PropertiesConfigurationFactory(target);
            factory.setPropertySource(loadProp(annotation.locations()));
            factory.setTargetName(annotation.prefix());

            factory.bindToTarget();

            return target;
        }

        private Properties loadProp(String... configLocations) {
            Properties prop = new Properties();
            prop.putAll(Env.getSystemEnvs());
            prop.putAll(Env.getEnvs());

            if (ArrayUtils.isEmpty(configLocations)) {
                ParamsHome.getInstance().coveredWithExtResource(prop);
                return prop;
            }


            for (String location : configLocations) {
                location = PropertyPlaceholderHelper.replacePlaceholder(location, true, Env.getEnvs());

                Resource resource = ResourceUtils.getResource(location);
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
            return prop;
        }
    }
}

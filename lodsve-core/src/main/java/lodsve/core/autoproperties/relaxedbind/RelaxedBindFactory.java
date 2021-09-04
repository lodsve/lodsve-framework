/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lodsve.core.autoproperties.relaxedbind;

import lodsve.core.autoproperties.Env;
import lodsve.core.autoproperties.ParamsHome;
import lodsve.core.autoproperties.env.Configuration;
import lodsve.core.autoproperties.env.PropertiesConfiguration;
import lodsve.core.autoproperties.relaxedbind.annotations.ConfigurationProperties;
import lodsve.core.autoproperties.relaxedbind.annotations.Required;
import lodsve.core.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 自动装配生成器.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016-1-26 14:17
 */
public class RelaxedBindFactory {
    private static final Logger logger = LoggerFactory.getLogger(RelaxedBindFactory.class);

    private static final List<?> COMMON_TYPES = Arrays.asList(Boolean.class, boolean.class, Long.class, long.class,
        Integer.class, int.class, String.class, Double.class, double.class, Resource.class, Properties.class,
        Class.class, File.class);

    private Properties propertySource;
    private final Object target;
    private String targetName;
    private Configuration configuration;
    private boolean readCache = true;

    private void setPropertySource(Properties propertySource) {
        Assert.notNull(propertySource);
        this.propertySource = propertySource;
        configuration = new PropertiesConfiguration(propertySource);

    }

    private void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    private void disabledReadCache() {
        this.readCache = false;
    }

    private static final Map<Class<?>, Object> CLASS_OBJECT_MAPPING = new HashMap<>(16);


    private RelaxedBindFactory(Object target) {
        this.target = target;
    }

    private void bindToTarget() {
        if (!readCache) {
            generateObject(targetName, target);
            return;
        }

        Object object = CLASS_OBJECT_MAPPING.get(target.getClass());
        if (object == null) {
            generateObject(targetName, target);
            CLASS_OBJECT_MAPPING.put(target.getClass(), target);

            return;
        }

        BeanUtils.copyProperties(object, target);
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
            if (null == typeDescriptor) {
                continue;
            }

            Required required = typeDescriptor.getAnnotation(Required.class);
            Object defaultValue = null;
            try {
                defaultValue = readMethod.invoke(target);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            Object value = getValue(type, prefix, name, readMethod);
            if (isEmpty(value)) {
                value = defaultValue;
            }

            if (value != null) {
                beanWrapper.setPropertyValue(name, value);
            } else if (required != null) {
                throw new RuntimeException(String.format("property [%s] in class [%s] can't be null!please check your config!", name, target.getClass().getName()));
            }
        }
    }

    private Object getValue(Class<?> type, String prefix, String name, Method readMethod) {
        String key = prefix, camelName = prefix;
        if (StringUtils.isNotBlank(name)) {
            key += ("." + name);
            camelName += ("." + getCamelName(name));
        }
        Object value = getValueForType(key, type, readMethod);

        if (isEmpty(value)) {
            value = getValueForType(camelName, type, readMethod);
        }

        return value;
    }

    private boolean isEmpty(Object value) {
        if (null == value) {
            return true;
        }

        if (value instanceof Collection) {
            return CollectionUtils.isEmpty((Collection) value);
        }

        if (value instanceof Map) {
            return MapUtils.isEmpty((Map) value);
        }

        if (value.getClass().isArray()) {
            return ArrayUtils.isEmpty((Object[]) value);
        }

        return false;
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
        } else if (type.isEnum()) {
            value = getValueForEnum(key, type);
        } else {
            if (type.isInterface()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("class [{}] is a interface!", type);
                }
                return null;
            }
            value = BeanUtils.instantiateClass(type);

            bindToSubTarget(value, key);
        }

        return value;
    }

    @SuppressWarnings("unchecked")
    private Enum<?> getValueForEnum(String key, Class<?> type) {
        String value = configuration.getString(key);
        if (StringUtils.isBlank(value)) {
            return null;
        }

        String text = PropertyPlaceholderHelper.replacePlaceholder(value, true);

        return Enum.valueOf((Class<? extends Enum>) type, text);
    }

    private void bindToSubTarget(Object target, String targetName) {
        RelaxedBindFactory factory = new RelaxedBindFactory(target);
        factory.setTargetName(targetName);
        factory.setPropertySource(propertySource);
        factory.disabledReadCache();
        factory.bindToTarget();
    }

    private Object getValueForSimpleType(String key, Class<?> type) {
        String value = configuration.getString(key);
        if (StringUtils.isBlank(value)) {
            return null;
        }

        String text = PropertyPlaceholderHelper.replacePlaceholder(value, true);

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
                return ClassUtils.forName(text, RelaxedBindFactory.class.getClassLoader());
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
            String keyInMap;
            Object value;
            if (COMMON_TYPES.contains(secondGenericClazz)) {
                keyInMap = StringUtils.removeStart(key, "[");
                keyInMap = StringUtils.removeEnd(keyInMap, "]");

                if (map.containsKey(keyInMap)) {
                    continue;
                }
                value = getValueForSimpleType(prefix + "." + key, secondGenericClazz);
            } else {
                String[] temp = StringUtils.split(key, ".");
                if (temp.length < 2) {
                    continue;
                }
                keyInMap = StringUtils.removeStart(temp[0], "[");
                keyInMap = StringUtils.removeEnd(keyInMap, "]");

                if (map.containsKey(keyInMap)) {
                    continue;
                }

                value = BeanUtils.instantiateClass(secondGenericClazz);
                bindToSubTarget(value, prefix + ".[" + keyInMap + "]");
            }


            if (value != null) {
                map.put(keyInMap, value);
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

        return list.toArray(new Object[0]);
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
        Assert.hasText(name, "name is required!");

        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            for (int i = 0; i < name.length(); i++) {
                String tmp = name.substring(i, i + 1);
                //判断截获的字符是否是大写，大写字母的toUpperCase()还是大写的
                if (!NumberUtils.isCreatable(tmp) && tmp.equals(tmp.toUpperCase())) {
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
        private final Class<T> clazz;

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

            RelaxedBindFactory factory = new RelaxedBindFactory(target);
            factory.setPropertySource(loadProp(annotation.locations()));
            factory.setTargetName(annotation.prefix());

            factory.bindToTarget();

            return target;
        }

        private Properties loadProp(String... configLocations) {
            Properties prop = new Properties();

            if (ArrayUtils.isEmpty(configLocations)) {
                prop.putAll(Env.getSystemEnvs());
                prop.putAll(Env.getEnvs());
                return prop;
            }

            for (String location : configLocations) {
                location = PropertyPlaceholderHelper.replacePlaceholder(location, true);

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

            prop.put("params.root", ParamsHome.getInstance().getParamsRoot());
            // 获取覆盖的值
            ParamsHome.getInstance().coveredWithExtResource(prop);
            return prop;
        }
    }
}

package message.jdbc.convert;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 类型转换.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-15 21:29
 */
public final class ConvertHelper {
    private static final Map<Class<?>, Class<?>> convertBeans = new HashMap<>();

    private ConvertHelper() {
    }

    public static void setConvertBeans(Map<Class<?>, Class<?>> convertBeans) {
        ConvertHelper.convertBeans.putAll(convertBeans);
    }

    public static <T extends ConvertGetter> Convert<T> getConvert(Class<T> clazz) {
        if (MapUtils.isEmpty(convertBeans)) {
            return null;
        }

        Class<?> convertBeanClazz = convertBeans.get(clazz);
        try {
            return (Convert<T>) BeanUtils.instantiateClass(convertBeanClazz.getConstructor(Class.class), clazz);
        } catch (NoSuchMethodException e) {
            throw new BeanInstantiationException(convertBeanClazz.getDeclaringClass(), "has no Constructor with arguments '" + Class.class.getSimpleName() + "'!", e);
        }
    }
}

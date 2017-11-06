package lodsve.core.utils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * object util class
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @createtime 2012-6-26 上午09:44:13
 */
public class ObjectUtils extends org.apache.commons.lang.ObjectUtils {
    /**
     * 默认方法名前缀
     */
    private static final String DEFAULT_METHOD_PREFIX = "get";

    private ObjectUtils() {
        super();
    }

    /**
     * 判断是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) throws Exception {
        return obj == null;
    }

    /**
     * 判断是否为非空
     *
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Object obj) throws Exception {
        return !isEmpty(obj);
    }

    /**
     * 获取object的class
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static Class<?> getClazz(Object obj) throws Exception {
        return isEmpty(obj) ? null : obj.getClass();
    }

    /**
     * 第一个object数组是否包含第二个object数组
     *
     * @param obj1 包含的数组           为空返回false
     * @param obj2 被包含的数组          为空返回false
     * @return
     */
    public static boolean contain(Object[] obj1, Object[] obj2) {
        if (obj1 == null || obj1.length < 1) {
            return false;
        }
        if (obj2 == null || obj2.length < 1) {
            return false;
        }
        List<Object> obj1List = Arrays.asList(obj1);
        List<Object> obj2List = Arrays.asList(obj2);

        return CollectionUtils.containsAny(obj1List, obj2List);
    }

    /**
     * 判断srcObj是否包含在destArray中
     *
     * @param destArray 目标数组        为空返回false
     * @param srcObj    源对象          为空返回false
     * @return
     */
    public static boolean contains(Object[] destArray, Object srcObj) {
        return contain(destArray, new Object[]{srcObj});
    }

    public static Map<String, Object> object2Map(Object obj) {
        try {
            if (obj == null) {
                return Collections.emptyMap();
            }

            Field[] fields = getFields(obj);
            Map<String, Object> map = new HashMap<>(fields.length);
            for (Field f : fields) {
                Object value = getFieldValue(obj, f.getName());
                map.put(f.getName(), value);
            }

            return map;
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    /**
     * 获取对象中的所有字段
     * getFields()与getDeclaredFields()区别:
     * getFields()只能访问类中声明为公有的字段,私有的字段它无法访问.
     * getDeclaredFields()能访问类中所有的字段,与public,private,protect无关
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static Field[] getFields(Object obj) throws Exception {
        if (isEmpty(obj)) {
            return new Field[0];
        }

        return obj.getClass().getDeclaredFields();
    }

    /**
     * 根据字段名得到实例的字段值
     *
     * @param object    实例对象
     * @param fieldName 字段名称
     * @return 实例字段的值，如果没找到该字段则返回null
     * @throws IllegalAccessException
     */
    public static Object getFieldValue(Object object, String fieldName) throws IllegalAccessException {
        Set<Field> fields = new HashSet<>();
        // 本类中定义的所有字段
        Field[] tempFields = object.getClass().getDeclaredFields();
        for (Field field : tempFields) {
            field.setAccessible(true);
            fields.add(field);
        }
        // 所有的public字段，包括父类中的
        tempFields = object.getClass().getFields();
        Collections.addAll(fields, tempFields);

        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                return field.get(object);
            }
        }
        return null;
    }

    /**
     * 合并obj2和obj2的值，并返回，以前一个对象为准
     *
     * @param first
     * @param second
     */
    public static Object mergerObject(Object first, Object second) throws Exception {
        if (first == null || second == null) {
            return null;
        }

        if (!first.getClass().equals(second.getClass())) {
            return null;
        }

        Class<?> clazz = first.getClass();
        Object result = BeanUtils.instantiate(clazz);
        Field[] fields = clazz.getDeclaredFields();

        for (Field f : fields) {
            //设置字段可读
            f.setAccessible(true);

            Object value1 = f.get(first);
            Object value2 = f.get(second);

            Object value = value1;
            if (value == null) {
                value = value2;
            }

            f.set(result, value);
        }

        return result;
    }
}

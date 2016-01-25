package message.base.utils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.MethodInvoker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
     * 获取object的class名(包名+类名)
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static String getClassName(Object obj) throws Exception {
        return isNotEmpty(obj) ? obj.getClass().getName() : StringUtils.EMPTY;
    }

    /**
     * 获取object的class名(仅类名)
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static String getSimpleClassName(Object obj) throws Exception {
        return isNotEmpty(obj) ? obj.getClass().getSimpleName() : StringUtils.EMPTY;
    }

    /**
     * 获取object的所有方法
     *
     * @param obj       对象
     * @param hasParent 是否包含父类的方法（true 包含；false 不包含）
     * @return
     * @throws Exception
     */
    public static Method[] getMethods(Object obj, boolean hasParent) throws Exception {
        if (isEmpty(obj)) {
            return null;
        }

        if (hasParent)
            return obj.getClass().getMethods();
        return obj.getClass().getDeclaredMethods();
    }

    /**
     * 获取object的所有方法(方法名)
     *
     * @param obj       对象
     * @param hasParent 是否包含父类的方法（true 包含；false 不包含）
     * @return
     * @throws Exception
     */
    public static String[] getMethodNames(Object obj, boolean hasParent) throws Exception {
        Method[] methods = getMethods(obj, hasParent);
        int len = methods.length;
        if (len < 1) {
            return null;
        }
        String[] names = new String[len];
        for (int i = 0; i < len; i++) {
            Method m = methods[i];
            names[i] = m.getName();
        }

        return names;
    }

    /**
     * 获取object的class
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static Class getClazz(Object obj) throws Exception {
        return isEmpty(obj) ? null : obj.getClass();
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
     * 获取对象中的所有字段(字段名)
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static String[] getFieldNames(Object obj) throws Exception {
        Field[] fields = getFields(obj);
        int len = fields.length;
        if (len < 1) {
            return null;
        }
        String[] f = new String[len];
        for (int i = 0; i < len; i++) {
            Field field = fields[i];
            f[i] = field.getName();
        }

        return f;
    }

    /**
     * 通过反射根据字段名和前缀取得字段的值
     *
     * @param obj       对象
     * @param fieldName 字段名
     * @param prefix    前缀
     * @return
     * @throws Exception
     */
    public static Object getValue(Object obj, String fieldName, String prefix) throws Exception {
        String methodName = createMethodName(fieldName, prefix);

        return getValue(obj, methodName);
    }

    /**
     * 通过反射根据字段名取得字段的值(前缀默认为"get")
     *
     * @param fieldName 字段名
     * @param obj       对象
     * @return
     * @throws Exception
     */
    public static Object getValue(String fieldName, Object obj) throws Exception {
        return getValue(obj, fieldName, DEFAULT_METHOD_PREFIX);
    }

    /**
     * 通过反射根据方法名取得字段的值
     *
     * @param obj        对象
     * @param methodName 方法名
     * @return
     * @throws Exception
     */
    public static Object getValue(Object obj, String methodName) throws Exception {
        if (obj == null || StringUtils.isEmpty(methodName)) {
            return null;
        }

        MethodInvoker methodInvoker = new MethodInvoker();
        methodInvoker.setTargetClass(getClazz(obj));
        //下来可以自己手工设置方法参数
        methodInvoker.setTargetMethod(methodName);
        methodInvoker.setTargetObject(obj);

        // 准备方法
        methodInvoker.prepare();

        return methodInvoker.invoke();
    }

    /**
     * 根据字段名和方法前缀拼出getter方法名
     *
     * @param fieldName 字段名
     * @param prefix    前缀
     * @return
     */
    public static String createMethodName(String fieldName, String prefix) {
        if (StringUtils.isEmpty(fieldName) || StringUtils.isEmpty(prefix)) {
            return StringUtils.EMPTY;
        }
        //判断fieldName第一位是否是小写
        String first = fieldName.substring(0, 1);
        if (!StringUtils.equals(first, first.toUpperCase())) {
            //第一个字符小写的
            first = first.toUpperCase();
        }
        String methodName = prefix + first + fieldName.substring(1);

        return methodName;
    }

    /**
     * 根据字段名得到实例的字段值
     *
     * @param object    实例对象
     * @param fieldName 字段名称
     * @return 实例字段的值，如果没找到该字段则返回null
     * @throws IllegalAccessException
     */
    public static Object getFieldValue(Object object, String fieldName)
            throws IllegalAccessException {
        Set<Field> fields = new HashSet<Field>();
        // 本类中定义的所有字段
        Field[] tempFields = object.getClass().getDeclaredFields();
        for (Field field : tempFields) {
            field.setAccessible(true);
            fields.add(field);
        }
        // 所有的public字段，包括父类中的
        tempFields = object.getClass().getFields();
        for (Field field : tempFields) {
            fields.add(field);
        }

        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                return field.get(object);
            }
        }
        return null;
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
        List<Object> obj1_ = Arrays.asList(obj1);
        List<Object> obj2_ = Arrays.asList(obj2);

        return CollectionUtils.containsAny(obj1_, obj2_);
    }

    /**
     * 判断srcObj是否包含在destArray中
     *
     * @param destArray 目标数组        为空返回false
     * @param srcObj    源对象          为空返回false
     * @return
     */
    public static boolean contain(Object[] destArray, Object srcObj) {
        return contain(destArray, new Object[]{srcObj});
    }

    /**
     * 获取一个controller类在应用中的module名
     *
     * @param controller controller类
     * @return
     */
    public static String[] getModules(Class controller) {
        //controller name
        String className = controller.getSimpleName();
        if (!className.endsWith("Controller")) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        if (className != null && className.length() > 0) {
            for (int i = 0; i < className.length(); i++) {
                String tmp = className.substring(i, i + 1);
                if (tmp.equals(tmp.toUpperCase())) {
                    //此字符是大写的
                    sb.append("_").append(tmp);
                } else {
                    sb.append(tmp);
                }
            }
            if (sb.toString().startsWith("_"))
                sb = new StringBuffer(sb.substring(1));
        }
        String tmp[] = sb.toString().split("_");
        List<String> names = new ArrayList<String>();

        for (int i = 0; i < tmp.length - 1; i++) {
            names.add(tmp[i].toLowerCase());
            if (i != 0) {
                String strTmp = "";
                for (int j = 0; j < i + 1; j++) {
                    if (j == 0)
                        strTmp += tmp[j].toLowerCase();
                    else
                        strTmp += tmp[j];
                }
                names.add(strTmp);
            }
        }

        return names.toArray(new String[]{});
    }

    /**
     * 获取一个controller类在应用中的module名(默认第一个)
     *
     * @param controller controller类
     * @return
     */
    public static String getModule(Class controller) {
        String[] names = getModules(controller);
        if (names != null && names.length > 0) {
            return names[0];
        }

        return StringUtils.EMPTY;
    }

    public static Map<String, Object> object2Map(Object obj) {
        try {
            if (obj == null)
                return Collections.emptyMap();

            Field[] fields = getFields(obj);
            Map<String, Object> map = new HashMap<String, Object>();
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
     * 合并obj2和obj2的值，并返回，以前一个对象为准
     *
     * @param first
     * @param second
     */
    public static Object mergerObject(Object first, Object second) throws Exception {
        if (first == null || second == null)
            return null;

        if (!first.getClass().equals(second.getClass()))
            return null;

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

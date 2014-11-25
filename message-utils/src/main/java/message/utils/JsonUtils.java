package message.utils;

import message.json.JSONSerializer;

/**
 * json的工具类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2012-7-9 下午09:32:57
 */
public class JsonUtils {

    /**
     * 私有化构造器
     */
    private JsonUtils() {
    }

    public static String toString(Object obj) {
        return toString(null, obj);
    }

    public static String toString(String rootName, Object obj) {
        JSONSerializer serializer = new JSONSerializer();
        //serializer.addExclude("*.class");
        serializer.exclude(new String[]{"class"});
        if (rootName == null) {
            return serializer.deepSerialize(obj);
        }
        return serializer.deepSerialize(rootName, obj);
    }
}

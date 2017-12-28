package lodsve.core.json;

import java.util.Map;

/**
 * JsonUtils.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2017-12-28-0028 14:28
 */
public interface JsonConverter {
    /**
     * convert object to json string
     *
     * @param obj object
     * @return json string
     */
    String toJson(Object obj);

    /**
     * convert json string to object
     *
     * @param json  json string
     * @param clazz object's class
     * @param <T>   class
     * @return object
     */
    <T> T toObject(String json, Class<T> clazz);

    /**
     * convert json string to map,key-value as field-value
     *
     * @param json json string
     * @return map
     */
    Map<String, Object> toMap(String json);
}

package lodsve.core.json;

import java.util.Map;

/**
 * .
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2017-12-28-0028 15:17
 */
public final class JsonUtils {
    public static JsonMode mode = JsonMode.JACKSON;

    private JsonUtils() {

    }

    /**
     * convert object to json string
     *
     * @param obj object
     * @return json string
     */
    public static String toJson(Object obj) {
        return getConverter().toJson(obj);
    }

    /**
     * convert json string to object
     *
     * @param json  json string
     * @param clazz object's class
     * @param <T>   class
     * @return object
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        return getConverter().toObject(json, clazz);
    }

    /**
     * convert json string to map,key-value as field-value
     *
     * @param json json string
     * @return map
     */
    public static Map<String, Object> toMap(String json) {
        return getConverter().toMap(json);
    }

    private static JsonConverter getConverter() {
        JsonConverter converter;

        switch (mode) {
            case GSON:
                converter = new GsonConverter();
                break;
            case JACKSON:
                converter = new JacksonConverter();
                break;
            case FastJson:
                converter = new FastJsonConverter();
                break;
            default:
                converter = new JacksonConverter();
        }

        return converter;
    }

    public enum JsonMode {
        JACKSON, GSON, FastJson
    }
}

/*
 * Copyright (C) 2018  Sun.Hao
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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

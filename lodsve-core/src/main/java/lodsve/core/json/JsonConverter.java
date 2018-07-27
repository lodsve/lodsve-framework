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
 * JsonUtils.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017-12-28-0028 14:28
 */
public interface JsonConverter {
    /**
     * convert object to json string, without format
     *
     * @param obj object
     * @return json string
     */
    String toJson(Object obj);

    /**
     * convert object to json string. You can choose whether to format
     *
     * @param obj    object
     * @param format whether to format
     * @return json string
     */
    String toJson(Object obj, boolean format);

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

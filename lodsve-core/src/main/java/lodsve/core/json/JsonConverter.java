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

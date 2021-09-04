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

/**
 * 获取JsonConverter的工厂.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017-12-28-0028 15:17
 */
public final class JsonConverterFactory {

    private JsonConverterFactory() {

    }

    /**
     * 获取JsonConverter
     *
     * @param mode json mode
     * @return JsonConverter
     * @see JsonMode
     * @see JsonConverter
     */
    public static JsonConverter getConverter(JsonMode mode) {
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

    /**
     * 使用json的类型
     */
    public enum JsonMode {
        /**
         * jackson
         */
        JACKSON,
        /**
         * google gson
         */
        GSON,
        /**
         * alibaba fastjson
         */
        FastJson
    }
}

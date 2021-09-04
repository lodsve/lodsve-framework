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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Gson Utils.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017-12-28-0028 14:57
 */
public class GsonConverter extends AbstractJsonConverter {
    private static final Gson GSON;

    static {
        GSON = new Gson();
    }

    @Override
    public String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    @Override
    public <T> T toObject(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    @Override
    public Map<String, Object> toMap(String json) {
        Type mapType = new TypeToken<Map<String, Object>>() {
        }.getType();

        return GSON.fromJson(json, mapType);
    }
}

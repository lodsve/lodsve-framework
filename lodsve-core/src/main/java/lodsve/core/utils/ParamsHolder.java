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
package lodsve.core.utils;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 参数放入ThreadLocal中,同一线程中,不用传递参数,即可使用.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 15/10/27 下午3:10
 */
public class ParamsHolder {
    private ParamsHolder() {
    }

    private static final ThreadLocal<Map<String, Object>> PARAMS_HOLDER = new ThreadLocal<>();

    public static void set(String key, Object object) {
        Assert.hasText(key);

        Map<String, Object> params = PARAMS_HOLDER.get();
        if (params == null) {
            params = new HashMap<>(1);
        }

        params.put(key, object);

        PARAMS_HOLDER.set(params);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        Map<String, Object> params = PARAMS_HOLDER.get();
        if (CollectionUtils.isEmpty(params)) {
            return null;
        }

        return (T) params.get(key);
    }

    public static void remove(String key) {
        Map<String, Object> params = PARAMS_HOLDER.get();
        if (CollectionUtils.isEmpty(params)) {
            return;
        }

        params.remove(key);
    }

    public static void removes() {
        PARAMS_HOLDER.remove();
    }

    public static void sets(Map<String, Object> params) {
        PARAMS_HOLDER.set(params);
    }

    public static Map<String, Object> gets() {
        return PARAMS_HOLDER.get();
    }
}

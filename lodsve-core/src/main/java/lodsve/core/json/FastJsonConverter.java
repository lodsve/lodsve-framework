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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * FastJson Utils.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2017-12-28-0028 14:43
 */
public class FastJsonConverter extends AbstractJsonConverter {
    @Override
    public String toJson(Object obj) {
        Assert.notNull(obj);

        return JSON.toJSONString(obj);
    }

    @Override
    public <T> T toObject(String json, Class<T> clazz) {
        Assert.hasText(json);
        Assert.notNull(clazz);

        return JSON.parseObject(json, clazz);
    }

    @Override
    public Map<String, Object> toMap(String json) {
        return JSON.parseObject(json, new TypeReference<Map<String, Object>>() {
        });
    }
}

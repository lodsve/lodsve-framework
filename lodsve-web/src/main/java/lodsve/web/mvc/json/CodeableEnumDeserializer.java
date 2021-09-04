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
package lodsve.web.mvc.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lodsve.core.bean.Codeable;
import lodsve.core.utils.EnumUtils;
import lodsve.core.utils.NumberUtils;
import lodsve.core.utils.StringUtils;
import lodsve.web.mvc.config.WebMvcConfiguration;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Jackson反序列化枚举时，将code或者枚举value变成枚举.<br/>
 * {@link WebMvcConfiguration#objectMapper()}
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2016/11/3 下午2:56
 */
public class CodeableEnumDeserializer extends JsonDeserializer<Enum> {
    @Override
    @SuppressWarnings("unchecked")
    public Enum deserialize(JsonParser p, DeserializationContext context) throws IOException {
        String value = p.getValueAsString();
        if (StringUtils.isBlank(value)) {
            return null;
        }

        Class<? extends Enum> clazz = getType(p);
        if (clazz == null) {
            return null;
        }

        if (!Codeable.class.isAssignableFrom(clazz)) {
            return getEnumByOrdinal(clazz, value);
        }

        // 优先根据codeable去获取
        Enum<?> result = getEnumFromCodeable(clazz, value);
        if (null != result) {
            return result;
        }

        try {
            // 根据枚举名称
            result = EnumUtils.getEnumByName(clazz, value);
        } catch (Exception e) {
            // 根据Ordinal
            result = getEnumByOrdinal(clazz, value);
        }

        return result;
    }

    @Override
    public Class<?> handledType() {
        return Enum.class;
    }

    @SuppressWarnings("unchecked")
    private <T extends Enum<T>> Class<T> getType(JsonParser p) throws IOException {
        Object object = p.getCurrentValue();
        Class<?> clazz = object.getClass();
        String fieldName = p.getCurrentName();

        Field field = ReflectionUtils.findField(clazz, fieldName);
        Class<?> type = ((null != field) ? field.getType() : null);

        if (support(type)) {
            return (Class<T>) type;
        }

        return null;
    }

    private boolean support(Class<?> clazz) {
        return clazz != null && Enum.class.isAssignableFrom(clazz) && Enum.class.isAssignableFrom(clazz);
    }

    private Enum<?> getEnumByOrdinal(Class<? extends Enum> clazz, String value) {
        if (!NumberUtils.isNumber(value)) {
            throw new IllegalArgumentException("This value " + value + " is not Enum's Ordinal!");
        }

        return EnumUtils.getEnumByOrdinal(clazz, Integer.valueOf(value));
    }

    private Enum<?> getEnumFromCodeable(Class<? extends Enum> clazz, String value) {
        for (Enum<?> em : clazz.getEnumConstants()) {
            Codeable codeable = (Codeable) em;

            if (codeable.getCode().equals(value)) {
                return em;
            }
        }

        return null;
    }
}

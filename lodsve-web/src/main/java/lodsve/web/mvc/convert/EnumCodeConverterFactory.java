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
package lodsve.web.mvc.convert;

import lodsve.core.bean.Codeable;
import lodsve.core.utils.StringUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * spring mvc将传递的参数转换成枚举.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2014-12-17 20:23
 */
public class EnumCodeConverterFactory implements ConverterFactory<String, Enum<?>>, ConditionalConverter {
    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        Class<?> clazz = targetType.getType();
        return Enum.class.isAssignableFrom(clazz) && Codeable.class.isAssignableFrom(clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Enum<?>> Converter<String, T> getConverter(Class<T> targetType) {
        if (!Codeable.class.isAssignableFrom(targetType)) {
            return null;
        }

        return new ValueToEnum(targetType);
    }

    private class ValueToEnum<T extends Enum<T> & Codeable> implements Converter<String, T> {
        private final T[] enums;
        private final Class<T> enumType;

        private ValueToEnum(Class<T> enumType) {
            this.enumType = enumType;
            this.enums = enumType.getEnumConstants();
        }

        @Override
        public T convert(String source) {
            if (StringUtils.isBlank(source)) {
                return null;
            }

            T result;
            try {
                result = Enum.valueOf(enumType, source);
            } catch (Exception e) {
                result = null;
            }

            if (result != null) {
                return result;
            }

            for (T em : enums) {
                if (em.getCode().equals(source)) {
                    result = em;
                    break;
                }
            }

            return result;
        }
    }
}

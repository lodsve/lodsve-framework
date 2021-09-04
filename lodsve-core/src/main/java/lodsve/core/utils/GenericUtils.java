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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

/**
 * 泛型工具类.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2015-1-6 16:20
 */
public class GenericUtils {
    private GenericUtils() {

    }

    public static <T> Class<T> getGenericParameter(Class clazz, int index) {
        Type genType = clazz.isInterface() ? clazz.getGenericInterfaces()[0] : clazz.getGenericSuperclass();
        if (genType instanceof ParameterizedType) {
            return getGenericParameter((ParameterizedType) genType, index);
        }
        return null;
    }

    public static <T> Class<T> getGenericParameter0(Class clazz) {
        return getGenericParameter(clazz, 0);
    }

    public static <T> Class<T> getGenericParameter(Field field, int index) {
        Type genType = field.getGenericType();
        if (genType instanceof ParameterizedType) {
            return getGenericParameter((ParameterizedType) genType, index);
        }
        return null;
    }

    public static <T> Class<T> getGenericParameter0(Field field) {
        return getGenericParameter(field, 0);
    }

    public static <T> Class<T> getGenericParameter(Method method, int index) {
        Type genType = method.getGenericReturnType();
        if (genType instanceof ParameterizedType) {
            return getGenericParameter((ParameterizedType) genType, index);
        }
        return null;
    }

    public static <T> Class<T> getGenericParameter0(Method method) {
        return getGenericParameter(method, 0);
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> getGenericParameter(ParameterizedType type, int index) {
        Type param = type.getActualTypeArguments()[index];
        if (param instanceof Class) {
            return (Class<T>) param;
        }
        return null;
    }

    @SafeVarargs
    public static Set<Field> getAnnotatedFields(Class clazz, Class<? extends Annotation>... annotations) {
        return doInAnnotatedFields(clazz, new SetFieldExtractor(), annotations);
    }

    @SafeVarargs
    public static Field getAnnotatedField(Class clazz, Class<? extends Annotation>... annotations) {
        return doInAnnotatedFields(clazz, new SingleFieldExtractor(), annotations);
    }

    @SafeVarargs
    public static <T> T doInAnnotatedFields(Class clazz, FieldExtractor<T> extractor, Class<? extends Annotation>... annotations) {
        for (Field field : clazz.getDeclaredFields()) {
            int hit = 0;
            for (Class<? extends Annotation> annotation : annotations) {
                if (field.isAnnotationPresent(annotation)) {
                    hit++;
                } else {
                    break;
                }
            }
            if (hit == annotations.length) {
                if (!extractor.extractNext(field)) {
                    return extractor.getReturn();
                }
            }
        }
        Class parent = clazz.getSuperclass();
        if (parent != null) {
            return doInAnnotatedFields(parent, extractor, annotations);
        }
        return extractor.getReturn();
    }

    private static class SetFieldExtractor implements FieldExtractor<Set<Field>> {
        Set<Field> result = new HashSet<>();

        @Override
        public boolean extractNext(Field field) {
            result.add(field);
            return true;
        }

        @Override
        public Set<Field> getReturn() {
            return result;
        }
    }

    private static class SingleFieldExtractor implements FieldExtractor<Field> {
        Field field = null;

        @Override
        public boolean extractNext(Field field) {
            this.field = field;
            return false;
        }

        @Override
        public Field getReturn() {
            return field;
        }
    }

    public interface FieldExtractor<T> {
        boolean extractNext(Field field);

        T getReturn();
    }
}

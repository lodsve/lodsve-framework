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

/**
 * 枚举相关工具类.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-11-26 22:55
 */
public class EnumUtils {
    /**
     * 根据枚举下标获取枚举值
     *
     * @param clazz   枚举类型
     * @param ordinal 下标
     * @return 枚举值
     */
    public static Enum<?> getEnumByOrdinal(Class<? extends Enum> clazz, Integer ordinal) {
        Assert.notNull(clazz, "enum class not be null!");

        Enum<?>[] enums = clazz.getEnumConstants();
        if (ordinal < 0 || ordinal >= enums.length) {
            throw new IndexOutOfBoundsException("Invalid ordinal");
        }

        return enums[ordinal];
    }

    /**
     * 根据枚举名称获取枚举值
     *
     * @param clazz 枚举类型
     * @param name  枚举值
     * @param <T>   枚举类型
     * @return 枚举值
     */
    public static <T extends Enum<T>> T getEnumByName(Class<T> clazz, String name) {
        return Enum.valueOf(clazz, name);
    }
}

/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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

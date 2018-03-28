/*
 * Copyright (C) 2018  Sun.Hao
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.core.utils;

/**
 * 类型转换器的接口.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0
 * @date 2015-2-9 22:07
 */
public interface StringParse<V> {
    /**
     * 转换
     *
     * @param str 待转换的数据
     * @return
     */
    V parse(String str);
}

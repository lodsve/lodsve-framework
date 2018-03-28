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

import java.util.Map;

/**
 * 检查excel中每一行的数据是否合法.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @date 14-2-25 上午10:01
 */
public interface CheckExcel {
    /**
     * 返回true合法
     *
     * @param data      excel中每一行的数据
     * @return
     */
    boolean check(Map<String, String> data);
}

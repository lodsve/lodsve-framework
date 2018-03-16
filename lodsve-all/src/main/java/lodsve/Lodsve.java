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

package lodsve;

import lodsve.core.LodsveVersion;

/**
 * Lodsve.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2018/1/16 下午9:47
 */
public class Lodsve {
    private Lodsve() {
    }

    /**
     * 获取当前版本号
     *
     * @return 当前版本号
     * @see LodsveVersion#getVersion()
     */
    public static String getVersion() {
        return LodsveVersion.getVersion();
    }
}

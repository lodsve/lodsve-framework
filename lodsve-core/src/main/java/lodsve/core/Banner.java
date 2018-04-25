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

package lodsve.core;

import lodsve.core.configuration.BannerConfig;

import java.io.PrintStream;

/**
 * Banner.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @date 2018/1/12 下午6:21
 */
public interface Banner {
    /**
     * 打印banner
     *
     * @param config banner config
     * @param out    out
     */
    void print(BannerConfig config, PrintStream out);
}

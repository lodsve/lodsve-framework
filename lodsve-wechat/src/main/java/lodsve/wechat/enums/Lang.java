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

package lodsve.wechat.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 语言枚举.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/3/2 下午2:10
 */
public enum Lang {
    /**
     * zh_CN 简体，zh_TW 繁体，en 英语
     */
    zh_CN, zh_TW, en;

    @JsonCreator
    public static Lang eval(String v) {
        switch (v) {
            case "zh_CN":
                return zh_CN;
            case "zh_TW":
                return zh_TW;
            case "en":
                return en;
            default:
                break;
        }
        return null;
    }
}

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

package lodsve.workflow.enums;

import lodsve.core.bean.Codeable;
import lodsve.core.utils.StringUtils;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/4/15 下午2:43
 */
public enum UrlType implements Codeable {
    VIEW("100", "查看URL"), UPDATE("101", "修改URL");

    private String code;
    private String title;

    UrlType(String code, String title) {
        this.code = code;
        this.title = title;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public static UrlType eval(String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }

        switch (type) {
            case "update":
                return UPDATE;
            case "view":
                return VIEW;
            default:
                throw new RuntimeException(String.format("can't eval given type: %s", type));
        }
    }
}

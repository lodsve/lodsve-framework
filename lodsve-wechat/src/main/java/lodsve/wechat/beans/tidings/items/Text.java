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

package lodsve.wechat.beans.tidings.items;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 文本.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/24 下午12:23
 */
@ApiModel(description = "文本")
public class Text {
    @ApiModelProperty(value = "内容", required = true)
    private String content;

    @ApiModelProperty(value = "内容", required = true)
    public String getContent() {
        return content;
    }

    @ApiModelProperty(value = "内容", required = true)
    public void setContent(String content) {
        this.content = content;
    }
}

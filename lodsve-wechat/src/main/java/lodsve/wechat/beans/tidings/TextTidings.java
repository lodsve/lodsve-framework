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

package lodsve.wechat.beans.tidings;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lodsve.wechat.beans.tidings.items.Text;

/**
 * 文本客服消息.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/24 上午11:56
 */
@ApiModel(description = "文本客服消息")
public class TextTidings extends Tidings {
    public TextTidings() {
        setTidingsType(TidingsType.text);
    }

    @ApiModelProperty(value = "文本客服消息", required = true)
    private Text text;

    @ApiModelProperty(value = "文本客服消息", required = true)
    public Text getText() {
        return text;
    }

    @ApiModelProperty(value = "文本客服消息", required = true)
    public void setText(Text text) {
        this.text = text;
    }
}

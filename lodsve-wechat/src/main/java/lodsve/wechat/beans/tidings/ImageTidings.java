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
import lodsve.wechat.beans.tidings.items.Media;

/**
 * 图片客服消息.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/2/24 上午11:57
 */
@ApiModel(description = "图片客服消息")
public class ImageTidings extends Tidings {
    public ImageTidings() {
        setTidingsType(TidingsType.image);
    }

    @ApiModelProperty(value = "图片", required = true)
    private Media image;

    @ApiModelProperty(value = "图片", required = true)
    public Media getImage() {
        return image;
    }

    @ApiModelProperty(value = "图片", required = true)
    public void setImage(Media image) {
        this.image = image;
    }
}

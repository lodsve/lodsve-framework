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
package lodsve.wechat.beans.tidings;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lodsve.wechat.beans.tidings.items.Media;

/**
 * 图文客服消息.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/24 下午12:17
 */
@ApiModel(description = "图文客服消息")
public class MpNewsTidings extends Tidings {
    public MpNewsTidings() {
        setTidingsType(TidingsType.mpnews);
    }

    @ApiModelProperty(value = "图文", required = true)
    private Media mpnews;

    @ApiModelProperty(value = "图文", required = true)
    public Media getMpnews() {
        return mpnews;
    }

    @ApiModelProperty(value = "图文", required = true)
    public void setMpnews(Media mpnews) {
        this.mpnews = mpnews;
    }
}

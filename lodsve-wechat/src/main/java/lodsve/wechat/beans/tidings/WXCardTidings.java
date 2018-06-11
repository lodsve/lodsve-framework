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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lodsve.wechat.beans.tidings.items.WXCard;

/**
 * 卡券客服消息.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/2/24 下午12:18
 */
@ApiModel(description = "卡券客服消息")
public class WXCardTidings extends Tidings {
    public WXCardTidings() {
        setTidingsType(TidingsType.wxcard);
    }

    @ApiModelProperty(value = "卡券", required = true)
    @JsonProperty("wxcard")
    private WXCard wxCard;

    @ApiModelProperty(value = "卡券", required = true)
    public WXCard getWxCard() {
        return wxCard;
    }

    @ApiModelProperty(value = "卡券", required = true)
    public void setWxCard(WXCard wxCard) {
        this.wxCard = wxCard;
    }
}

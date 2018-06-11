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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 卡券.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/2/24 下午12:24
 */
@ApiModel(description = "卡券")
public class WXCard {
    @ApiModelProperty(value = "卡券ID", required = true)
    @JsonProperty("card_id")
    private String cardId;
    @ApiModelProperty(value = "卡券自定义消息", required = true)
    @JsonProperty("card_ext")
    private String cardExt;

    @ApiModelProperty(value = "卡券ID", required = true)
    public String getCardId() {
        return cardId;
    }

    @ApiModelProperty(value = "卡券ID", required = true)
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    @ApiModelProperty(value = "卡券自定义消息", required = true)
    public String getCardExt() {
        return cardExt;
    }

    @ApiModelProperty(value = "卡券自定义消息", required = true)
    public void setCardExt(String cardExt) {
        this.cardExt = cardExt;
    }
}

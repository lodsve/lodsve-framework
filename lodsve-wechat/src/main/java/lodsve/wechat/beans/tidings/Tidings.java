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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 客服消息.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/24 上午11:53
 */
@ApiModel(description = "客服消息")
public class Tidings {
    @ApiModelProperty(value = "接受者openId", required = true)
    @JsonProperty("touser")
    private String toUser;
    @ApiModelProperty(value = "消息类型", required = true)
    @JsonProperty("msgtype")
    private TidingsType tidingsType;
    @ApiModelProperty(value = "发送的客服号", required = true)
    @JsonProperty("customservice")
    private CustomService customService;

    @ApiModelProperty(value = "接受者openId", required = true)
    public String getToUser() {
        return toUser;
    }

    @ApiModelProperty(value = "接受者openId", required = true)
    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    @ApiModelProperty(value = "消息类型", required = true)
    public TidingsType getTidingsType() {
        return tidingsType;
    }

    @ApiModelProperty(value = "消息类型", required = true)
    public void setTidingsType(TidingsType tidingsType) {
        this.tidingsType = tidingsType;
    }

    @ApiModelProperty(value = "发送的客服号", required = true)
    public CustomService getCustomService() {
        return customService;
    }

    @ApiModelProperty(value = "发送的客服号", required = true)
    public void setCustomService(CustomService customService) {
        this.customService = customService;
    }

    @ApiModel(description = "发送的客服号")
    public static class CustomService {
        @ApiModelProperty(value = "发送的客服号", required = true)
        @JsonProperty("kf_account")
        private String account;

        @ApiModelProperty(value = "发送的客服号", required = true)
        public String getAccount() {
            return account;
        }

        @ApiModelProperty(value = "发送的客服号", required = true)
        public void setAccount(String account) {
            this.account = account;
        }
    }
}

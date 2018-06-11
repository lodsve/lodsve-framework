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

package lodsve.wechat.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 客服人员.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/2/24 上午10:00
 */
@ApiModel(description = "客服人员")
public class CustomerServer {
    @ApiModelProperty(value = "id", required = true)
    @JsonProperty("kf_id")
    private String id;
    @ApiModelProperty(value = "账号", required = true)
    @JsonProperty("kf_account")
    private String account;
    @ApiModelProperty(value = "昵称", required = true)
    @JsonProperty("kf_nick")
    private String nick;
    @ApiModelProperty(value = "头像", required = true)
    @JsonProperty("kf_headimgurl")
    private String headImgUrl;
    @ApiModelProperty(value = "密码", required = true)
    @JsonProperty("password")
    private String password;

    @ApiModelProperty(value = "id", required = true)
    public String getId() {
        return id;
    }

    @ApiModelProperty(value = "id", required = true)
    public void setId(String id) {
        this.id = id;
    }

    @ApiModelProperty(value = "账号", required = true)
    public String getAccount() {
        return account;
    }

    @ApiModelProperty(value = "账号", required = true)
    public void setAccount(String account) {
        this.account = account;
    }

    @ApiModelProperty(value = "昵称", required = true)
    public String getNick() {
        return nick;
    }

    @ApiModelProperty(value = "昵称", required = true)
    public void setNick(String nick) {
        this.nick = nick;
    }

    @ApiModelProperty(value = "头像", required = true)
    public String getHeadImgUrl() {
        return headImgUrl;
    }

    @ApiModelProperty(value = "头像", required = true)
    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    @ApiModelProperty(value = "密码", required = true)
    public String getPassword() {
        return password;
    }

    @ApiModelProperty(value = "密码", required = true)
    public void setPassword(String password) {
        this.password = password;
    }
}

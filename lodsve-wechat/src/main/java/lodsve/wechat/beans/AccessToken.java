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
package lodsve.wechat.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 保存token.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/21 下午5:40
 */
@ApiModel(description = "微信API每次访问的token")
public class AccessToken {
    @ApiModelProperty(value = "token", required = true)
    @JsonProperty("access_token")
    private String accessToken;
    @ApiModelProperty(value = "失效时长", required = true)
    @JsonProperty("expires_in")
    private int expiresIn;

    @ApiModelProperty(value = "token", required = true)
    public String getAccessToken() {
        return accessToken;
    }

    @ApiModelProperty(value = "token", required = true)
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @ApiModelProperty(value = "失效时长", required = true)
    public int getExpiresIn() {
        return expiresIn;
    }

    @ApiModelProperty(value = "失效时长", required = true)
    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}

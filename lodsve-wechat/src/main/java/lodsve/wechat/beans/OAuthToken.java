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
 * oauth认证的票据.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午3:01
 */
@ApiModel(description = "oauth认证的票据")
public class OAuthToken {
    @ApiModelProperty(value = "票据", required = true)
    @JsonProperty("access_token")
    private String accessToken;
    @ApiModelProperty(value = "有效时长", required = true)
    @JsonProperty("expires_in")
    private long expiresIn;
    @ApiModelProperty(value = "刷新票据", required = true)
    @JsonProperty("refresh_token")
    private String refreshToken;
    @ApiModelProperty(value = "openId", required = true)
    @JsonProperty("openid")
    private String openId;
    @ApiModelProperty(value = "作用域", required = true)
    private String scope;
    @ApiModelProperty(value = "unionId", required = true)
    @JsonProperty("unionid")
    private String unionId;

    @ApiModelProperty(value = "票据", required = true)
    public String getAccessToken() {
        return accessToken;
    }

    @ApiModelProperty(value = "票据", required = true)
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @ApiModelProperty(value = "有效时长", required = true)
    public long getExpiresIn() {
        return expiresIn;
    }

    @ApiModelProperty(value = "有效时长", required = true)
    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    @ApiModelProperty(value = "刷新票据", required = true)
    public String getRefreshToken() {
        return refreshToken;
    }

    @ApiModelProperty(value = "刷新票据", required = true)
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @ApiModelProperty(value = "openId", required = true)
    public String getOpenId() {
        return openId;
    }

    @ApiModelProperty(value = "openId", required = true)
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @ApiModelProperty(value = "作用域", required = true)
    public String getScope() {
        return scope;
    }

    @ApiModelProperty(value = "作用域", required = true)
    public void setScope(String scope) {
        this.scope = scope;
    }

    @ApiModelProperty(value = "unionId", required = true)
    public String getUnionId() {
        return unionId;
    }

    @ApiModelProperty(value = "unionId", required = true)
    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }
}

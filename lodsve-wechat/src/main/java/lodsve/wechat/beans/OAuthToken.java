/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lodsve.wechat.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * oauth认证的票据.
 *
 * @author sunhao(sunhao.java @ gmail.com)
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

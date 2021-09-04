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

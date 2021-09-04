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
 * 客服人员.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/24 上午10:00
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

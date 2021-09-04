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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lodsve.wechat.enums.Lang;

/**
 * 用户查询参数封装.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/3/7 下午10:48
 */
@ApiModel(description = "用户查询参数封装")
public class UserQuery {
    /**
     * 用户的标识，对当前公众号唯一
     */
    @ApiModelProperty(value = "用户的标识", required = true)
    private String openId;
    /**
     * 用户的语言，简体中文为zh_CN
     */
    @ApiModelProperty(value = "用户的语言", required = true)
    private Lang language;

    @ApiModelProperty(value = "用户的标识", required = true)
    public String getOpenId() {
        return openId;
    }

    @ApiModelProperty(value = "用户的标识", required = true)
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @ApiModelProperty(value = "用户的语言", required = true)
    public Lang getLanguage() {
        return language;
    }

    @ApiModelProperty(value = "用户的语言", required = true)
    public void setLanguage(Lang language) {
        this.language = language;
    }
}

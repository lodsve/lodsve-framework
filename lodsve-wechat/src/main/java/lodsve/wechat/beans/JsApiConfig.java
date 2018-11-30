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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 通过JS-API调用的一些配置.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/23 上午11:55
 */
@ApiModel(description = "通过JS-API调用的一些配置")
public class JsApiConfig {
    @ApiModelProperty(value = "appId", required = true)
    private String appId;
    @ApiModelProperty(value = "时间戳", required = true)
    private long timestamp;
    @ApiModelProperty(value = "随机字符串", required = true)
    private String nonceStr;
    @ApiModelProperty(value = "签名", required = true)
    private String signature;

    @ApiModelProperty(value = "appId", required = true)
    public String getAppId() {
        return appId;
    }

    @ApiModelProperty(value = "appId", required = true)
    public void setAppId(String appId) {
        this.appId = appId;
    }

    @ApiModelProperty(value = "时间戳", required = true)
    public long getTimestamp() {
        return timestamp;
    }

    @ApiModelProperty(value = "时间戳", required = true)
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @ApiModelProperty(value = "随机字符串", required = true)
    public String getNonceStr() {
        return nonceStr;
    }

    @ApiModelProperty(value = "随机字符串", required = true)
    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    @ApiModelProperty(value = "签名", required = true)
    public String getSignature() {
        return signature;
    }

    @ApiModelProperty(value = "签名", required = true)
    public void setSignature(String signature) {
        this.signature = signature;
    }
}

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

/**
 * 模板消息的数据.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/24 下午4:24
 */
@ApiModel(description = "模板消息的数据")
public class TemplateData {
    @ApiModelProperty(value = "键", required = true)
    private String key;
    @ApiModelProperty(value = "值", required = true)
    private String value;
    @ApiModelProperty(value = "文字颜色", required = true)
    private String color;

    @ApiModelProperty(value = "键", required = true)
    public String getKey() {
        return key;
    }

    @ApiModelProperty(value = "键", required = true)
    public void setKey(String key) {
        this.key = key;
    }

    @ApiModelProperty(value = "值", required = true)
    public String getValue() {
        return value;
    }

    @ApiModelProperty(value = "值", required = true)
    public void setValue(String value) {
        this.value = value;
    }

    @ApiModelProperty(value = "文字颜色", required = true)
    public String getColor() {
        return color;
    }

    @ApiModelProperty(value = "文字颜色", required = true)
    public void setColor(String color) {
        this.color = color;
    }
}

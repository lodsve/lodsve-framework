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
import lodsve.wechat.enums.Lang;

/**
 * 用户查询参数封装.
 *
 * @author sunhao(sunhao.java@gmail.com)
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

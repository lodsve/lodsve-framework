/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
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
 * 微信分组.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/3/2 上午11:09
 */
@ApiModel(description = "微信分组")
public class Group {
    /**
     * 分组id，由微信分配
     */
    @ApiModelProperty(value = "id", required = true)
    private int id;
    /**
     * 分组名字，UTF8编码
     */
    @ApiModelProperty(value = "分组名字", required = true)
    private String name;
    /**
     * 分组内用户数量
     */
    @ApiModelProperty(value = "分组内用户数量", required = true)
    private int count;

    @ApiModelProperty(value = "id", required = true)
    public int getId() {
        return id;
    }

    @ApiModelProperty(value = "id", required = true)
    public void setId(int id) {
        this.id = id;
    }

    @ApiModelProperty(value = "分组名字", required = true)
    public String getName() {
        return name;
    }

    @ApiModelProperty(value = "分组名字", required = true)
    public void setName(String name) {
        this.name = name;
    }

    @ApiModelProperty(value = "分组内用户数量", required = true)
    public int getCount() {
        return count;
    }

    @ApiModelProperty(value = "分组内用户数量", required = true)
    public void setCount(int count) {
        this.count = count;
    }
}

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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 行业信息.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/3/4 下午1:36
 */
@ApiModel(description = "行业信息")
public class Industry {
    @ApiModelProperty(value = "主要行业信息", required = true)
    @JsonProperty("primary_industry")
    private IndustryInfo primary;
    @ApiModelProperty(value = "次要行业信息", required = true)
    @JsonProperty("secondary_industry")
    private IndustryInfo secondary;

    @ApiModelProperty(value = "主要行业信息", required = true)
    public IndustryInfo getPrimary() {
        return primary;
    }

    @ApiModelProperty(value = "主要行业信息", required = true)
    public void setPrimary(IndustryInfo primary) {
        this.primary = primary;
    }

    @ApiModelProperty(value = "次要行业信息", required = true)
    public IndustryInfo getSecondary() {
        return secondary;
    }

    @ApiModelProperty(value = "次要行业信息", required = true)
    public void setSecondary(IndustryInfo secondary) {
        this.secondary = secondary;
    }

    @ApiModel(description = "行业信息")
    public static class IndustryInfo {
        @ApiModelProperty(value = "第一编码", required = true)
        @JsonProperty("first_class")
        private String first;
        @ApiModelProperty(value = "第二编码", required = true)
        @JsonProperty("second_class")
        private String second;

        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public String getSecond() {
            return second;
        }

        public void setSecond(String second) {
            this.second = second;
        }
    }
}

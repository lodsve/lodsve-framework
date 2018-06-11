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

package lodsve.wechat.beans.tidings.items;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 媒体消息.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/2/24 下午12:21
 */
@ApiModel(description = "媒体消息")
public class Media {
    @ApiModelProperty(value = "媒体消息ID", required = true)
    @JsonProperty("media_id")
    private String mediaId;

    @ApiModelProperty(value = "媒体消息ID", required = true)
    public String getMediaId() {
        return mediaId;
    }

    @ApiModelProperty(value = "媒体消息ID", required = true)
    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}

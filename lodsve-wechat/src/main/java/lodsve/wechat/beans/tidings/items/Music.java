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
package lodsve.wechat.beans.tidings.items;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 音乐.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/24 下午12:22
 */
@ApiModel(description = "音乐")
public class Music {
    @ApiModelProperty(value = "标题", required = true)
    private String title;
    @ApiModelProperty(value = "描述", required = true)
    private String description;
    @ApiModelProperty(value = "音乐url", required = true)
    @JsonProperty("musicurl")
    private String musicUrl;
    @ApiModelProperty(value = "高品质音乐链接", required = true)
    @JsonProperty("hqmusicurl")
    private String hqMusicUrl;
    @ApiModelProperty(value = "缩略图的媒体ID", required = true)
    @JsonProperty("thumb_media_id")
    private String thumbMediaId;

    @ApiModelProperty(value = "标题", required = true)
    public String getTitle() {
        return title;
    }

    @ApiModelProperty(value = "标题", required = true)
    public void setTitle(String title) {
        this.title = title;
    }

    @ApiModelProperty(value = "描述", required = true)
    public String getDescription() {
        return description;
    }

    @ApiModelProperty(value = "描述", required = true)
    public void setDescription(String description) {
        this.description = description;
    }

    @ApiModelProperty(value = "音乐url", required = true)
    public String getMusicUrl() {
        return musicUrl;
    }

    @ApiModelProperty(value = "音乐url", required = true)
    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    @ApiModelProperty(value = "高品质音乐链接", required = true)
    public String getHqMusicUrl() {
        return hqMusicUrl;
    }

    @ApiModelProperty(value = "高品质音乐链接", required = true)
    public void setHqMusicUrl(String hqMusicUrl) {
        this.hqMusicUrl = hqMusicUrl;
    }

    @ApiModelProperty(value = "缩略图的媒体ID", required = true)
    public String getThumbMediaId() {
        return thumbMediaId;
    }

    @ApiModelProperty(value = "缩略图的媒体ID", required = true)
    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }
}

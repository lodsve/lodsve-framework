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
package lodsve.wechat.beans.tidings;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lodsve.wechat.beans.tidings.items.Video;

/**
 * 视频客服消息.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 16/2/24 下午12:01
 */
@ApiModel(description = "视频客服消息")
public class VideoTidings extends Tidings {
    public VideoTidings() {
        setTidingsType(TidingsType.video);
    }

    @ApiModelProperty(value = "视频", required = true)
    private Video video;

    @ApiModelProperty(value = "视频", required = true)
    public Video getVideo() {
        return video;
    }

    @ApiModelProperty(value = "视频", required = true)
    public void setVideo(Video video) {
        this.video = video;
    }
}

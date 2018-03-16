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

package lodsve.wechat.beans.tidings;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lodsve.wechat.beans.tidings.items.Media;

/**
 * 语音客服消息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 上午11:59
 */
@ApiModel(description = "语音客服消息")
public class VoiceTidings extends Tidings {
    public VoiceTidings() {
        setTidingsType(TidingsType.voice);
    }

    @ApiModelProperty(value = "语音", required = true)
    private Media voice;

    @ApiModelProperty(value = "语音", required = true)
    public Media getVoice() {
        return voice;
    }

    @ApiModelProperty(value = "语音", required = true)
    public void setVoice(Media voice) {
        this.voice = voice;
    }
}

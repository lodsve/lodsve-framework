/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lodsve.core.configuration.properties;

import lodsve.core.ansi.AnsiOutput;
import lombok.Getter;
import lombok.Setter;

/**
 * 控制台打印参数配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
@Getter
@Setter
public class AnsiConfig {
    /**
     * 是否启用
     */
    private AnsiOutput.Enabled enabled = AnsiOutput.Enabled.ALWAYS;
    /**
     * 控制台是否支持
     */
    private Boolean consoleAvailable = null;
}

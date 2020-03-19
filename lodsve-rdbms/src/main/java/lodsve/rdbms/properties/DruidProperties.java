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

package lodsve.rdbms.properties;

import com.alibaba.druid.wall.WallConfig;
import lodsve.core.properties.relaxedbind.annotations.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * druid配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018-2-8-0008 15:05
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "lodsve.druid", locations = "${params.root}/framework/druid.properties")
public class DruidProperties {
    private String filters = "stat\\,wall";
    private WallConfig wallConfig;
    private boolean enableMonitor = false;
    private String path = "/druid/*";
    private String resetEnable = "true";
    private String user;
    private String password;
    private String allow;
    private String deny;
    private String remoteAddress;
}

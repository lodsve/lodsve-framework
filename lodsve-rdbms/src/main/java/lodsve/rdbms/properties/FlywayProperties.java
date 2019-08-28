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

import lodsve.core.properties.relaxedbind.annotations.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * flyway配置.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "lodsve.flyway", locations = "${params.root}/framework/flyway.properties")
public class FlywayProperties {
    /**
     * flyway的脚本文件所在路径
     * key: 数据源名称
     * value: flyway脚本路径，多个以逗号分隔
     */
    private Map<String, String> locations;
}

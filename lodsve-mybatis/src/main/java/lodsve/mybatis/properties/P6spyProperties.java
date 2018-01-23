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

package lodsve.mybatis.properties;

import lodsve.core.properties.relaxedbind.annotations.ConfigurationProperties;
import org.springframework.core.io.Resource;

/**
 * Spy Properties.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 2017/12/25 下午11:53
 */
@ConfigurationProperties(prefix = "lodsve.p6spy", locations = "${params.root}/framework/spy.properties")
public class P6spyProperties {
    /**
     * spy配置
     */
    private Resource config;

    public Resource getConfig() {
        return config;
    }

    public void setConfig(Resource config) {
        this.config = config;
    }
}

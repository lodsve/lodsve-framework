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

/**
 * .
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @date 2018-2-8-0008 16:47
 */
@ConfigurationProperties(prefix = "lodsve.mybatis", locations = "${params.root}/framework/mybatis.properties")
public class MyBatisProperties {
    private String[] dataSources;

    public String[] getDataSources() {
        return dataSources;
    }

    public void setDataSources(String[] dataSources) {
        this.dataSources = dataSources;
    }
}

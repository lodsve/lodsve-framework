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

package lodsve.mongodb.properties;

import lodsve.core.properties.relaxedbind.annotations.ConfigurationProperties;

import java.util.Map;

/**
 * mongodb base properties.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 16/1/21 下午6:55
 */
@ConfigurationProperties(prefix = "lodsve.mongo", locations = "${params.root}/framework/mongo.properties")
public class MongoProperties {
    private int maxpoolsize = 10;
    private Map<String, MongoConnection> project;

    public int getMaxpoolsize() {
        return maxpoolsize;
    }

    public void setMaxpoolsize(int maxpoolsize) {
        this.maxpoolsize = maxpoolsize;
    }

    public Map<String, MongoConnection> getProject() {
        return project;
    }

    public void setProject(Map<String, MongoConnection> project) {
        this.project = project;
    }
}

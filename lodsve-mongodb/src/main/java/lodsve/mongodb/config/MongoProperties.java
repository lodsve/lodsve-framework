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

package lodsve.mongodb.config;

import lodsve.core.properties.autoconfigure.annotations.ConfigurationProperties;

import java.util.Map;

/**
 * mongodb base properties.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/1/21 下午6:55
 */
@ConfigurationProperties(prefix = "lodsve.mongo", locations = "${params.root}/framework/mongo.properties")
public class MongoProperties {
    private int maxpoolsize;
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

    public static class MongoConnection{
        private String url;
        private String username;
        private String password;
        private int maxpoolsize;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getMaxpoolsize() {
            return maxpoolsize;
        }

        public void setMaxpoolsize(int maxpoolsize) {
            this.maxpoolsize = maxpoolsize;
        }
    }
}

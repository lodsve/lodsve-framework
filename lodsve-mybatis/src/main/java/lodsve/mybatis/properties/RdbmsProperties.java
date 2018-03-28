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
import lodsve.core.properties.relaxedbind.annotations.Required;

import java.util.Map;

/**
 * rdbms base properties,only support BasicDataSource and DruidDataSource.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2016-1-27 09:20
 */
@ConfigurationProperties(prefix = "lodsve.rdbms", locations = "${params.root}/framework/rdbms.properties")
public class RdbmsProperties {
    /**
     * 数据源类型
     */
    private String dataSourceClass = "org.apache.commons.dbcp.BasicDataSource";
    /**
     * 连接信息
     */
    private Map<String, PoolSetting> pool;

    public String getDataSourceClass() {
        return dataSourceClass;
    }

    public void setDataSourceClass(String dataSourceClass) {
        this.dataSourceClass = dataSourceClass;
    }

    public Map<String, PoolSetting> getPool() {
        return pool;
    }

    public void setPool(Map<String, PoolSetting> pool) {
        this.pool = pool;
    }

    public static class PoolSetting {
        private String driverClassName = "com.mysql.jdbc.Driver";
        @Required
        private String url;
        @Required
        private String username;
        @Required
        private String password;
        private Integer initialSize = 10;
        private Integer maxActive = 100;
        private Integer minIdle = 20;
        private Integer maxWait = 60000;
        private Boolean removeAbandoned = true;
        private Integer removeAbandonedTimeout = 180;
        private Boolean testOnBorrow = true;
        private Boolean testOnReturn = true;
        private Boolean testWhileIdle = false;
        private String validationQuery = "select 1";
        private Integer maxIdle = 5;

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

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

        public Integer getInitialSize() {
            return initialSize;
        }

        public void setInitialSize(Integer initialSize) {
            this.initialSize = initialSize;
        }

        public Integer getMaxActive() {
            return maxActive;
        }

        public void setMaxActive(Integer maxActive) {
            this.maxActive = maxActive;
        }

        public Integer getMinIdle() {
            return minIdle;
        }

        public void setMinIdle(Integer minIdle) {
            this.minIdle = minIdle;
        }

        public Integer getMaxWait() {
            return maxWait;
        }

        public void setMaxWait(Integer maxWait) {
            this.maxWait = maxWait;
        }

        public Boolean getRemoveAbandoned() {
            return removeAbandoned;
        }

        public void setRemoveAbandoned(Boolean removeAbandoned) {
            this.removeAbandoned = removeAbandoned;
        }

        public Integer getRemoveAbandonedTimeout() {
            return removeAbandonedTimeout;
        }

        public void setRemoveAbandonedTimeout(Integer removeAbandonedTimeout) {
            this.removeAbandonedTimeout = removeAbandonedTimeout;
        }

        public Boolean getTestOnBorrow() {
            return testOnBorrow;
        }

        public void setTestOnBorrow(Boolean testOnBorrow) {
            this.testOnBorrow = testOnBorrow;
        }

        public Boolean getTestOnReturn() {
            return testOnReturn;
        }

        public void setTestOnReturn(Boolean testOnReturn) {
            this.testOnReturn = testOnReturn;
        }

        public Boolean getTestWhileIdle() {
            return testWhileIdle;
        }

        public void setTestWhileIdle(Boolean testWhileIdle) {
            this.testWhileIdle = testWhileIdle;
        }

        public String getValidationQuery() {
            return validationQuery;
        }

        public void setValidationQuery(String validationQuery) {
            this.validationQuery = validationQuery;
        }

        public Integer getMaxIdle() {
            return maxIdle;
        }

        public void setMaxIdle(Integer maxIdle) {
            this.maxIdle = maxIdle;
        }
    }
}

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

import java.util.Map;

/**
 * rdbms base properties,only support BasicDataSource and DruidDataSource.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-1-27 09:20
 */
@ConfigurationProperties(prefix = "lodsve.rdbms", locations = "${params.root}/framework/rdbms.properties")
public class RdbmsProperties {
    /**
     * 数据源类型
     */
    private String dataSourceClass;
    /**
     * 通用配置
     */
    private DataSourceSetting commons;
    /**
     * dbcp配置
     */
    private DbcpSetting dbcp;
    /**
     * druid数据源的配置
     */
    private DruidSetting druid;
    /**
     * 连接信息
     */
    private Map<String, RdbmsConnection> connections;

    public String getDataSourceClass() {
        return dataSourceClass;
    }

    public void setDataSourceClass(String dataSourceClass) {
        this.dataSourceClass = dataSourceClass;
    }

    public DataSourceSetting getCommons() {
        return commons;
    }

    public void setCommons(DataSourceSetting commons) {
        this.commons = commons;
    }

    public DbcpSetting getDbcp() {
        return dbcp;
    }

    public void setDbcp(DbcpSetting dbcp) {
        this.dbcp = dbcp;
    }

    public DruidSetting getDruid() {
        return druid;
    }

    public void setDruid(DruidSetting druid) {
        this.druid = druid;
    }

    public Map<String, RdbmsConnection> getConnections() {
        return connections;
    }

    public void setConnections(Map<String, RdbmsConnection> connections) {
        this.connections = connections;
    }

    public static class DataSourceSetting {
        private Integer initialSize;
        private Integer maxActive;
        private Integer minIdle;
        private Integer maxWait;
        private Boolean removeAbandoned;
        private Integer removeAbandonedTimeout;
        private Boolean testOnBorrow;
        private Boolean testOnReturn;
        private Boolean testWhileIdle;
        private String validationQuery;

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
    }

    public static class DbcpSetting {
        private Integer maxIdle;

        public Integer getMaxIdle() {
            return maxIdle;
        }

        public void setMaxIdle(Integer maxIdle) {
            this.maxIdle = maxIdle;
        }
    }

    public static class DruidSetting {
        private String filters;

        public String getFilters() {
            return filters;
        }

        public void setFilters(String filters) {
            this.filters = filters;
        }
    }

    public static class RdbmsConnection {
        private String driverClassName;
        private String url;
        private String username;
        private String password;

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
    }
}

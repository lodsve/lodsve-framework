package lodsve.redis.core.config;

import lodsve.core.config.auto.annotations.ConfigurationProperties;

import java.util.Map;

/**
 * redis的配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/9/8 下午3:49
 */
@ConfigurationProperties(prefix = "lodsve.redis", locations = "file:${params.root}/files/redis.properties")
public class RedisProperties {
    private Pool pool;
    private Map<String, ProjectSetting> project;

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public Map<String, ProjectSetting> getProject() {
        return project;
    }

    public void setProject(Map<String, ProjectSetting> project) {
        this.project = project;
    }

    public static class ProjectSetting {
        private String url = "redis://localhost:6379/0";
        private String password;
        private int timeout = 100000;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }
    }

    /**
     * Pool properties.
     */
    public static class Pool {

        private int maxIdle = 200;
        private int minIdle = 10;
        private int maxWait = 60000;
        private int maxTotal = 1024;
        private boolean testOnBorrow = true;
        private boolean testOnReturn = true;
        private boolean testWhileIdle = true;

        public int getMaxIdle() {
            return maxIdle;
        }

        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }

        public int getMinIdle() {
            return minIdle;
        }

        public void setMinIdle(int minIdle) {
            this.minIdle = minIdle;
        }

        public int getMaxWait() {
            return maxWait;
        }

        public void setMaxWait(int maxWait) {
            this.maxWait = maxWait;
        }

        public int getMaxTotal() {
            return maxTotal;
        }

        public void setMaxTotal(int maxTotal) {
            this.maxTotal = maxTotal;
        }

        public boolean isTestOnBorrow() {
            return testOnBorrow;
        }

        public void setTestOnBorrow(boolean testOnBorrow) {
            this.testOnBorrow = testOnBorrow;
        }

        public boolean isTestOnReturn() {
            return testOnReturn;
        }

        public void setTestOnReturn(boolean testOnReturn) {
            this.testOnReturn = testOnReturn;
        }

        public boolean isTestWhileIdle() {
            return testWhileIdle;
        }

        public void setTestWhileIdle(boolean testWhileIdle) {
            this.testWhileIdle = testWhileIdle;
        }
    }
}

package lodsve.core.configuration;

import lodsve.core.properties.autoconfigure.annotations.ConfigurationProperties;

/**
 * 系统配置.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2016/12/12 下午3:05
 */
@ConfigurationProperties(prefix = "application", locations = "${params.root}/framework/application.properties")
public class ApplicationProperties {
    /**
     * 开发模式
     */
    private boolean devMode;
    private ThreadConfig thread;

    public boolean isDevMode() {
        return devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public ThreadConfig getThread() {
        return thread;
    }

    public void setThread(ThreadConfig thread) {
        this.thread = thread;
    }

    public static class ThreadConfig {
        private int corePoolSize = 1;
        private int maxPoolSize = Integer.MAX_VALUE;
        private int keepAliveSeconds = 60;
        private boolean allowCoreThreadTimeOut = false;
        private int queueCapacity = Integer.MAX_VALUE;
        private boolean exposeUnconfigurableExecutor = false;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getKeepAliveSeconds() {
            return keepAliveSeconds;
        }

        public void setKeepAliveSeconds(int keepAliveSeconds) {
            this.keepAliveSeconds = keepAliveSeconds;
        }

        public boolean isAllowCoreThreadTimeOut() {
            return allowCoreThreadTimeOut;
        }

        public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
            this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
        }

        public int getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }

        public boolean isExposeUnconfigurableExecutor() {
            return exposeUnconfigurableExecutor;
        }

        public void setExposeUnconfigurableExecutor(boolean exposeUnconfigurableExecutor) {
            this.exposeUnconfigurableExecutor = exposeUnconfigurableExecutor;
        }
    }
}

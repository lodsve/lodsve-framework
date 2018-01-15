package lodsve.properties;

import java.util.List;

/**
 * 系统配置.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 15/10/10 下午10:01
 */
public class ServerProperties {
    /**
     * 前台URL
     */
    private String frontEndUrl;
    /**
     * 服务端URL
     */
    private String serverUrl;
    /**
     * debug配置
     */
    private Debug debug;

    public String getFrontEndUrl() {
        return frontEndUrl;
    }

    public void setFrontEndUrl(String frontEndUrl) {
        this.frontEndUrl = frontEndUrl;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public Debug getDebug() {
        return debug;
    }

    public void setDebug(Debug debug) {
        this.debug = debug;
    }

    public static class Debug {
        /**
         * 需要忽略的url
         */
        private List<String> excludeUrl;
        /**
         * 需要忽略的ip/address
         */
        private List<String> excludeAddress;
        /**
         * 当执行时间超长，将会警告
         */
        private long maxProcessingTime = 3000;

        public List<String> getExcludeUrl() {
            return excludeUrl;
        }

        public void setExcludeUrl(List<String> excludeUrl) {
            this.excludeUrl = excludeUrl;
        }

        public List<String> getExcludeAddress() {
            return excludeAddress;
        }

        public void setExcludeAddress(List<String> excludeAddress) {
            this.excludeAddress = excludeAddress;
        }

        public long getMaxProcessingTime() {
            return maxProcessingTime;
        }

        public void setMaxProcessingTime(long maxProcessingTime) {
            this.maxProcessingTime = maxProcessingTime;
        }
    }
}

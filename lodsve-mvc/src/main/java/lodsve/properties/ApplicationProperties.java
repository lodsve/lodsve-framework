package lodsve.properties;

import lodsve.core.config.auto.annotations.ConfigurationProperties;

/**
 * 系统配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/10/10 下午10:01
 */
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
    /**
     * 开发模式
     */
    private boolean devMode;
    /**
     * 前台URL
     */
    private String frontEndUrl;
    /**
     * 服务端URL
     */
    private String serverUrl;

    public boolean isDevMode() {
        return devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

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
}

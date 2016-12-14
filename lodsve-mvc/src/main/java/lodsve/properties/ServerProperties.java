package lodsve.properties;

import lodsve.core.config.annotations.ConfigurationProperties;

/**
 * 系统配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/10/10 下午10:01
 */
@ConfigurationProperties(prefix = "server", locations = "file:${params.root}/framework/server.properties")
public class ServerProperties {
    /**
     * 前台URL
     */
    private String frontEndUrl;
    /**
     * 服务端URL
     */
    private String serverUrl;

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

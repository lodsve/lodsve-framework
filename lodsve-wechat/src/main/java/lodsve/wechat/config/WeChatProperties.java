package lodsve.wechat.config;

import lodsve.base.config.auto.annotations.ConfigurationProperties;

/**
 * 参数配置在配置文件中.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/21 下午4:58
 */
@ConfigurationProperties(prefix = "lodsve.wechat")
public class WeChatProperties {
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 应用密钥
     */
    private String appSecret;
    /**
     * Token
     */
    private String token;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

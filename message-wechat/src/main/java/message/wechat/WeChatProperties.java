package message.wechat;

import message.config.auto.annotations.ConfigurationProperties;

/**
 * 微信一些参数配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-1-27 12:53
 */
@ConfigurationProperties(prefix = "cosmos.wechat")
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
     * 令牌
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

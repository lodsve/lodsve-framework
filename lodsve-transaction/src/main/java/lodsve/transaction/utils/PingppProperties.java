package lodsve.transaction.utils;

import lodsve.core.config.auto.annotations.ConfigurationProperties;

/**
 * 支付的一些配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/8/26 上午4:01
 */
@ConfigurationProperties(prefix = "lodsve.ping.config", locations = "file:${params.root}/framework/pingpp.properties")
public class PingppProperties {
    /**
     * ping++的apiKey
     */
    private String apiKey;
    /**
     * ping++的appId
     */
    private String appId;
    /**
     * 支付完成后跳转地址
     */
    private String notifyUrl;
    /**
     * Ping++ 公钥
     */
    private String pubKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }
}

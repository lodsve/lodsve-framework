package message.transaction.utils;

import java.util.Map;

/**
 * 支付的一些配置.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/8/26 上午4:01
 */
public class PingConfig {
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
     * 阿里wap支付后回调
     */
    private Map<String, AliPayWapNotify> notifySettings;
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

    public Map<String, AliPayWapNotify> getNotifySettings() {
        return notifySettings;
    }

    public void setNotifySettings(Map<String, AliPayWapNotify> notifySettings) {
        this.notifySettings = notifySettings;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public static class AliPayWapNotify {
        private String from;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }
    }
}

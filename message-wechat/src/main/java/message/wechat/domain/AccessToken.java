package message.wechat.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 请求凭证.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-1-27 13:40
 */
public class AccessToken {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private long expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
}

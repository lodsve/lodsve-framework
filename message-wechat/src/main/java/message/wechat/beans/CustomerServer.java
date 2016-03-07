package message.wechat.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 客服人员.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 上午10:00
 */
public class CustomerServer {
    @JsonProperty("kf_id")
    private String id;
    @JsonProperty("kf_account")
    private String account;
    @JsonProperty("kf_nick")
    private String nick;
    @JsonProperty("kf_headimgurl")
    private String headImgUrl;
    @JsonProperty("password")
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

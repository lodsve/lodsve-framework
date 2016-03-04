package message.wechat.beans.tidings;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 客服消息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 上午11:53
 */
public class Tidings {
    @JsonProperty("touser")
    private String toUser;
    @JsonProperty("msgtype")
    private TidingsType tidingsType;
    @JsonProperty("customservice")
    private CustomService customService;

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public TidingsType getTidingsType() {
        return tidingsType;
    }

    public void setTidingsType(TidingsType tidingsType) {
        this.tidingsType = tidingsType;
    }

    public static class CustomService {
        @JsonProperty("kf_account")
        private String account;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }
    }
}

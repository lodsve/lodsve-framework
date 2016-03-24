package message.wechat.beans.tidings;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 客服消息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/24 上午11:53
 */
@ApiModel(description = "客服消息")
public class Tidings {
    @ApiModelProperty(value = "接受者openId", required = true)
    @JsonProperty("touser")
    private String toUser;
    @ApiModelProperty(value = "消息类型", required = true)
    @JsonProperty("msgtype")
    private TidingsType tidingsType;
    @ApiModelProperty(value = "发送的客服号", required = true)
    @JsonProperty("customservice")
    private CustomService customService;

    @ApiModelProperty(value = "接受者openId", required = true)
    public String getToUser() {
        return toUser;
    }

    @ApiModelProperty(value = "接受者openId", required = true)
    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    @ApiModelProperty(value = "消息类型", required = true)
    public TidingsType getTidingsType() {
        return tidingsType;
    }

    @ApiModelProperty(value = "消息类型", required = true)
    public void setTidingsType(TidingsType tidingsType) {
        this.tidingsType = tidingsType;
    }

    @ApiModelProperty(value = "发送的客服号", required = true)
    public CustomService getCustomService() {
        return customService;
    }

    @ApiModelProperty(value = "发送的客服号", required = true)
    public void setCustomService(CustomService customService) {
        this.customService = customService;
    }

    @ApiModel(description = "发送的客服号")
    public static class CustomService {
        @ApiModelProperty(value = "发送的客服号", required = true)
        @JsonProperty("kf_account")
        private String account;

        @ApiModelProperty(value = "发送的客服号", required = true)
        public String getAccount() {
            return account;
        }

        @ApiModelProperty(value = "发送的客服号", required = true)
        public void setAccount(String account) {
            this.account = account;
        }
    }
}

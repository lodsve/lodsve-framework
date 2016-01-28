package message.wechat.exception;

/**
 * 微信返回的异常.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-1-27 13:42
 */
public class WeChatException {
    private String errCode;
    private String errMsg;

    public WeChatException(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}

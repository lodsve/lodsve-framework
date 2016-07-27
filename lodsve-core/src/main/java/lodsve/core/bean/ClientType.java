package lodsve.core.bean;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/2/23 下午12:43
 */
public enum ClientType implements Codeable {
    UNKNOWN("101", "未知"),
    BROWSER("102", "浏览器"),
    WEIXIN("103", "微信"),
    QQ("104", "QQ"),
    ALIPAY("105", "支付宝"),
    APP("106", "客户端"),;

    private String code;
    private String title;

    ClientType(String code, String title) {
        this.code = code;
        this.title = title;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getTitle() {
        return title;
    }
}

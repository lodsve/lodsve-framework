package lodsve.transaction.enums;

import lodsve.core.bean.Codeable;

/**
 * 交易渠道.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/8/26 上午4:12
 */
public enum TradeChannel implements Codeable {
    ALIPAY("101", "支付宝手机支付"),
    ALIPAY_WAP("102", "支付宝手机网页支付"),
    ALIPAY_QR("103", "支付宝扫码支付"),
    APPLE_PAY("104", "APPLE PAY"),
    BFB("105", "百度钱包移动快捷支付"),
    BFB_WAP("106", "百度钱包手机网页支付"),
    UPACP("107", "银联全渠道支付"),
    UPACP_WAP("108", "银联全渠道手机网页支付"),
    UPMP("109", "银联手机支付"),
    UPMP_WAP("110", "银联手机网页支付"),
    WX("111", "微信支付"),
    WX_PUB("112", "微信公众账号支付"),
    WX_PUB_QR("113", "微信公众账号扫码支付"),
    YEEPAY_WAP("114", "易宝手机网页支付"),
    JDPAY_WAP("115", "京东手机网页支付"),
    FLOWER_PAY("116", "橘花钱包支付");

    private String code;
    private String title;

    TradeChannel(String code, String title) {
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

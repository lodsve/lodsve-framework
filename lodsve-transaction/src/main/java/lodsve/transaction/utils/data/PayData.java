package lodsve.transaction.utils.data;

import lodsve.transaction.enums.TradeChannel;

import java.io.Serializable;
import java.util.Map;

/**
 * 这里放着所有支付方式(使用ping++)需要用到的额外信息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/8/28 下午1:21
 */
public class PayData implements Serializable {
    public static final String RN_CHECK = "rn_check";
    public static final String LIMIT_PAY = "limit_pay";
    public static final String OPEN_ID = "openId";

    /**
     * 支付渠道
     */
    private TradeChannel tradeChannel;
    /**
     * 支付单号
     */
    private Long paymentId;
    /**
     * 支付金额
     */
    private Long amount;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品描述
     */
    private String payDesc;
    /** 以上是公共字段 **/
    /**
     * 额外参数
     */
    private Map<String, String> extra;

    public TradeChannel getTradeChannel() {
        return tradeChannel;
    }

    public void setTradeChannel(TradeChannel tradeChannel) {
        this.tradeChannel = tradeChannel;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPayDesc() {
        return payDesc;
    }

    public void setPayDesc(String payDesc) {
        this.payDesc = payDesc;
    }

    public Map<String, String> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, String> extra) {
        this.extra = extra;
    }
}

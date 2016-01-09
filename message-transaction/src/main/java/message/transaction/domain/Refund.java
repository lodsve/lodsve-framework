package message.transaction.domain;

import message.transaction.enums.TradeResult;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 退款.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/11/5 下午5:55
 */
@Table(name = "t_refund")
public class Refund {
    @Id
    @GeneratedValue(generator = "s_refund")
    private Long id;
    @Column
    private String chargeId;
    @Column
    private Long paymentId;
    @Column
    private Long amount;
    @Column
    private TradeResult tradeResult;
    @Column
    private Date applyTime;
    @Column
    private Date completeTime;
    /**
     * 支付宝退款链接
     */
    @Column
    private String refundUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
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

    public TradeResult getTradeResult() {
        return tradeResult;
    }

    public void setTradeResult(TradeResult tradeResult) {
        this.tradeResult = tradeResult;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public String getRefundUrl() {
        return refundUrl;
    }

    public void setRefundUrl(String refundUrl) {
        this.refundUrl = refundUrl;
    }
}

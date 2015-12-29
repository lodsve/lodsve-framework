package message.transaction.domain;

import message.transaction.enums.TradeResult;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 退款.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/11/5 下午5:55
 */
@Table(name = "t_refund")
public class Refund {
    @Id
    private Long id;
    @Column
    private String chargeId;
    @Column
    private Long paymentId;
    @Column
    private Long amount;
    @Column(name = "pay_status")
    private TradeResult result;
    @Column
    private Long applyTime;
    @Column
    private Long completeTime;
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

    public TradeResult getResult() {
        return result;
    }

    public void setResult(TradeResult result) {
        this.result = result;
    }

    public Long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Long applyTime) {
        this.applyTime = applyTime;
    }

    public Long getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Long completeTime) {
        this.completeTime = completeTime;
    }

    public String getRefundUrl() {
        return refundUrl;
    }

    public void setRefundUrl(String refundUrl) {
        this.refundUrl = refundUrl;
    }
}

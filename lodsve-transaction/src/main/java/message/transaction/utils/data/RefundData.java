package message.transaction.utils.data;

import message.transaction.domain.Payment;

/**
 * 退款数据.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/11/5 下午5:38
 */
public class RefundData {
    /**
     * 原支付单
     */
    private Payment payment;

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}

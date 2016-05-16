package lodsve.transaction.action;

import lodsve.transaction.domain.Payment;
import lodsve.transaction.domain.Refund;

/**
 * 退款.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/11/5 下午7:33
 */
public interface RefundAction {
    /**
     * 退款之前要做的
     *
     * @param payment 要退款的付款单
     */
    void beforeRefund(Payment payment);

    /**
     * 创建退款单
     *
     * @param paymentId 要退款的付款单ID
     * @return
     * @throws Exception
     */
    void refund(Long paymentId) throws Exception;

    /**
     * 退款成功回调
     *
     * @param refund 退款单
     */
    void doRefundSuccess(Refund refund);

    /**
     * 退款失败回调
     *
     * @param refund 退款单
     */
    void doRefundFail(Refund refund);
}

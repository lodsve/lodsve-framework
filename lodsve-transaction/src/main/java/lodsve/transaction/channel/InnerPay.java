package lodsve.transaction.channel;

import lodsve.transaction.domain.Payment;
import lodsve.transaction.domain.Refund;
import lodsve.transaction.notify.TradeNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 内部橘花支付.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/11/5 下午3:05
 */
@Component
public abstract class InnerPay<C, R> implements Pay<C, R> {
    @Autowired
    private TradeNotifyService tradeNotifyService;

    /**
     * 支付成功回调
     *
     * @param payment 支付单
     */
    public void doPaySuccess(Payment payment) {
        tradeNotifyService.payCallback(payment.getId() + "", Double.valueOf(payment.getAmount()), "", true);
        paySuccess(payment);
    }

    /**
     * 支付失败回调
     *
     * @param payment 支付单
     */
    public void doPayFail(Payment payment) {
        tradeNotifyService.payCallback(payment.getId() + "", Double.valueOf(payment.getAmount()), "", false);
        payFail(payment);
    }

    /**
     * 支付成功回调
     *
     * @param payment 支付单
     */
    public abstract void paySuccess(Payment payment);

    /**
     * 支付失败回调
     *
     * @param payment 支付单
     */
    public abstract void payFail(Payment payment);

    /**
     * 退款成功回调
     *
     * @param refund 退款单
     */
    public abstract void refundSuccess(Refund refund);

    /**
     * 退款失败回调
     *
     * @param refund 退款单
     */
    public abstract void refundFail(Refund refund);

    /**
     * 退款成功回调
     *
     * @param refund 退款单
     */
    public void doRefundSuccess(Refund refund) {
        tradeNotifyService.refundCallback(refund.getChargeId(), Double.valueOf(refund.getAmount()), true);
        refundSuccess(refund);
    }

    /**
     * 退款失败回调
     *
     * @param refund 退款单
     */
    public void doRefundFail(Refund refund) {
        tradeNotifyService.refundCallback(refund.getChargeId(), Double.valueOf(refund.getAmount()), false);
        refundFail(refund);
    }
}

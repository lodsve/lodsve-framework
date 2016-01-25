package message.transaction.notify;

import message.transaction.action.PayAction;
import message.transaction.action.RefundAction;
import message.transaction.domain.Payment;
import message.transaction.domain.Refund;
import message.transaction.enums.TradeResult;
import message.transaction.repository.PaymentRepository;
import message.transaction.repository.RefundRepository;
import message.transaction.utils.TradeRouting;
import message.utils.NumberUtils;
import message.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * 支付后回调.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/11/5 下午3:48
 */
@Component
public class TradeNotifyService {
    private static final Logger logger = LoggerFactory.getLogger(TradeNotifyService.class);

    @Autowired
    private RefundRepository refundRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private TradeRouting tradeRouting;

    /**
     * 支付后回调
     *
     * @param paymentId 支付单号
     * @param amount    金额
     * @param account   支付账户
     * @param result    结果
     * @return
     */
    @Transactional
    public boolean payCallback(String paymentId, Double amount, String account, boolean result) {
        // 支付单ID
        if (!NumberUtils.isNumber(paymentId)) {
            return false;
        }
        Payment payment = this.paymentRepository.select(Long.valueOf(paymentId));
        if (payment == null) {
            // 兼容以前的数据,以前数据,给ping++的order_no是orderId
            // 根据orderId获取支付单
            payment = this.paymentRepository.findByTargetId(paymentId);
        }

        if (null == payment || TradeResult.PROCESSING != payment.getTradeResult()) {
            throw new RuntimeException("支付单不存在。请确认数据完整性,需要查询的支付单号为：" + paymentId);
        }

        if (TradeResult.YES.equals(payment.getTradeResult())) {
            logger.info("订单支付状态已经成功,返回信息不需要重复处理,支付单号为:" + paymentId);
            return false;
        }

        PayAction payAction = this.tradeRouting.channel(payment.getTradeType());
        Assert.notNull(payAction, "消费类型异常!");

        if (amount == null || amount.longValue() != payment.getAmount().longValue()) {
            throw new RuntimeException("支付错误，支付金额被篡改，请检查支付数据,payId=" + paymentId);
        }

        // 支付账户
        if (StringUtils.isNotBlank(account) && !"null".equalsIgnoreCase(account))
            payment.setPayAccount(account);

        try {
            if (result) {
                // 修改支付单的支付状态
                payment.setTradeResult(TradeResult.YES);
                payment.setCompleteTime(new Date());
                this.paymentRepository.insert(payment);

                payAction.doPaySuccess(payment);
            } else {
                payment.setTradeResult(TradeResult.ERROR);
                // 修改支付单的支付状态
                this.paymentRepository.insert(payment);

                payAction.doPayFail(payment);
            }

            return true;
        } catch (Exception e) {
            logger.error("更新订单状态失败", e);
            payment.setTradeResult(TradeResult.ERROR);
            // 修改支付单的支付状态
            this.paymentRepository.insert(payment);

            payAction.doPayFail(payment);
            return false;
        }
    }

    /**
     * 退款后回调
     *
     * @param chargeId 支付单ID
     * @param amount   退款金额
     * @param result   结果
     * @return
     */
    @Transactional
    public boolean refundCallback(String chargeId, Double amount, boolean result) {
        // 支付单ID
        if (StringUtils.isBlank(chargeId)) {
            return false;
        }

        Refund refund = refundRepository.findByChargeId(chargeId);

        if (null == refund || TradeResult.PROCESSING != refund.getTradeResult()) {
            throw new RuntimeException("退款单不存在,请确认!支付单号为:" + chargeId);
        }

        if (TradeResult.YES.equals(refund.getTradeResult())) {
            logger.info("退款单已完成,支付单号为:" + chargeId);
            return false;
        }

        Payment payment = this.paymentRepository.findByChargeId(chargeId);

        if (amount == null || amount.longValue() != refund.getAmount() || payment == null || amount.longValue() != payment.getAmount()) {
            throw new RuntimeException("退款金额错误!支付单号为=" + chargeId);
        }

        RefundAction refundAction = this.tradeRouting.refund(payment.getTradeType());
        Assert.notNull(refundAction, "消费类型异常!");

        try {
            if (result) {
                // 修改支付单的支付状态
                refund.setTradeResult(TradeResult.YES);
                refund.setCompleteTime(new Date());

                this.refundRepository.insert(refund);

                refundAction.doRefundSuccess(refund);
            } else {
                payment.setTradeResult(TradeResult.ERROR);
                // 修改支付单的支付状态
                this.paymentRepository.insert(payment);

                refundAction.doRefundFail(refund);
            }

            return true;
        } catch (Exception e) {
            logger.error("更新订单状态失败", e);
            payment.setTradeResult(TradeResult.ERROR);
            // 修改支付单的支付状态
            this.paymentRepository.insert(payment);

            refundAction.doRefundFail(refund);
            return false;
        }
    }
}

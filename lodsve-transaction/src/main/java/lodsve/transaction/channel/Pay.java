package lodsve.transaction.channel;

import lodsve.transaction.domain.Refund;
import lodsve.transaction.utils.data.PayData;
import lodsve.transaction.utils.data.RefundData;

import java.util.Map;

/**
 * 支付.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/11/5 下午2:57
 */
public interface Pay<C, R> {
    /**
     * 支付之前检查权限
     *
     * @param amount     金额
     * @param extraParam 额外参数
     */
    void validatePay(Long amount, Map<String, Object> extraParam);

    /**
     * 发起支付
     *
     * @param payData 各种支付方式的额外参数
     * @return
     * @throws Exception
     */
    C pay(PayData payData) throws Exception;

    /**
     * 发起退款
     *
     * @param refundData 各种支付方式的额外参数
     * @return
     * @throws Exception
     */
    R refund(RefundData refundData) throws Exception;

    /**
     * 发起退款后修改退款单状态
     *
     * @param pingRefund ping++返回
     * @param refund     退款单
     */
    void changeRefundStatus(com.pingplusplus.model.Refund pingRefund, Refund refund);
}

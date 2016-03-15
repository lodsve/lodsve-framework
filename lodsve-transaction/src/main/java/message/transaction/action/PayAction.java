package message.transaction.action;

import message.transaction.domain.Payment;
import message.transaction.enums.TradeChannel;
import message.transaction.enums.TradeType;

import java.util.Map;

/**
 * 支付后回调.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/9/21 下午12:02
 */
public interface PayAction {

    /**
     * 支付之前要做的
     *
     * @param targetId 要支付对象的ID
     */
    void beforePayment(String targetId);

    /**
     * 创建支付单并预支付
     *
     * @param targetId     要支付对象的ID
     * @param tradeType    交易类型
     * @param tradeChannel 支付渠道
     * @param userId       用户ID
     * @param extra        额外参数
     * @return
     * @throws Exception
     */
    Object pay(String targetId, TradeType tradeType, TradeChannel tradeChannel, Long userId, Map<String, Object> extra) throws Exception;

    /**
     * 支付成功回调
     *
     * @param payment 支付单
     */
    void doPaySuccess(Payment payment);

    /**
     * 支付失败回调
     *
     * @param payment 支付单
     */
    void doPayFail(Payment payment);
}

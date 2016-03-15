package message.transaction.channel;

import message.transaction.domain.Refund;
import message.transaction.enums.TradeResult;
import org.springframework.stereotype.Component;

/**
 * 微信支付通用退款功能.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/11/6 下午12:49
 */
@Component
public abstract class CommonWXPay extends PingPay {
    @Override
    public void changeRefundStatus(com.pingplusplus.model.Refund pingRefund, Refund refund) {
        boolean succeed = pingRefund.getSucceed();
        String status = pingRefund.getStatus();

        if ("pending".equals(status) && !succeed) {
            // 退款中
            refund.setTradeResult(TradeResult.PROCESSING);
        } else {
            refund.setTradeResult(TradeResult.ERROR);
        }
    }
}

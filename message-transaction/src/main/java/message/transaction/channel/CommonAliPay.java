package message.transaction.channel;

import com.pingplusplus.model.Refund;
import message.transaction.enums.TradeResult;
import org.springframework.stereotype.Component;

/**
 * alipay支付通用退款功能.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/11/6 下午12:47
 */
@Component
public abstract class CommonAliPay extends PingPay {
    @Override
    public void changeRefundStatus(Refund pingRefund, message.transaction.domain.Refund refund) {
        String status = pingRefund.getStatus();
        String refundUrl = pingRefund.getFailureMsg();

        if ("pending".equals(status)) {
            // 退款中
            refund.setResult(TradeResult.PROCESSING);
            refund.setRefundUrl(refundUrl);
        } else {
            refund.setResult(TradeResult.ERROR);
        }
    }
}

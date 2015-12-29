package message.transaction.channel;

import message.transaction.enums.TradeChannel;
import message.transaction.utils.Channel;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * 支付宝支付.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/8/28 上午11:21
 */
@Component
@Channel(TradeChannel.ALIPAY)
public class AliPay extends CommonAliPay {
    @Override
    protected Map<String, String> buildExtraData() {
        return Collections.emptyMap();
    }
}

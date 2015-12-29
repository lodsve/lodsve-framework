package message.transaction.channel;

import message.transaction.enums.TradeChannel;
import message.transaction.utils.Channel;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * 微信支付.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/10/8 上午11:20
 */
@Component
@Channel(TradeChannel.WX)
public class WeiXinPay extends CommonWXPay {
    @Override
    protected Map<String, String> buildExtraData() {
        return Collections.emptyMap();
    }
}

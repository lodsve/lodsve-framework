package message.transaction.channel;

import message.transaction.enums.TradeChannel;
import message.transaction.utils.Channel;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * 微信公众号支付.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/8/28 上午11:21
 */
@Component
@Channel(TradeChannel.WX_PUB)
public class WeiXinPubPay extends CommonWXPay {
    @Override
    protected Map<String, String> buildExtraData() {
        // TODO
//        String openId = SessionUtils.getWeixinOpenid();

        return Collections.singletonMap("open_id", "");
    }
}

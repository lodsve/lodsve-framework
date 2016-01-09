package message.transaction.channel;

import message.transaction.enums.TradeChannel;
import message.transaction.utils.Channel;
import message.utils.ParamsHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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
        String openId = ParamsHolder.get("openId");
        Assert.hasText(openId, "微信OpenID必须指定!");

        return Collections.singletonMap("open_id", openId);
    }
}

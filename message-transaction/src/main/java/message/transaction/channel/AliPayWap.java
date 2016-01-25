package message.transaction.channel;

import message.transaction.enums.TradeChannel;
import message.transaction.enums.TradeType;
import message.transaction.utils.Channel;
import message.transaction.utils.PingConfig;
import message.utils.ParamsHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝手机网页支付.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/9/25 下午3:07
 */
@Component
@Channel(TradeChannel.ALIPAY_WAP)
public class AliPayWap extends CommonAliPay {
    @Autowired
    private PingConfig pingConfig;

    @Override
    protected Map<String, String> buildExtraData() {
        Map<String, String> params = new HashMap<>();

        TradeType type = ParamsHolder.get("tradeType");

        if (type == null) {
            throw new RuntimeException("TradeType IS NULL!");
        }

        String notifyUrl = pingConfig.getNotifyUrl() + "/%s/" + type.toString();
        params.put("success_url", String.format(notifyUrl, "success"));
        params.put("cancel_url", String.format(notifyUrl, "cancel"));

        return params;
    }
}
